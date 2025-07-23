package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ProfilePictureDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UpdateUserDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserPageDto;
import br.com.ifsp.tsi.bugtrackerbackend.exception.PasswordException;
import br.com.ifsp.tsi.bugtrackerbackend.exception.ProfilePictureException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.*;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.UserRole;
import br.com.ifsp.tsi.bugtrackerbackend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final TicketRepository ticketRepository;
    private final MessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(
            UserRepository userRepository,
            RatingRepository ratingRepository,
            TicketRepository ticketRepository,
            MessageRepository messageRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with username: " + email));

        return new UserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getEmail(),
                user.getPassword(),
                user.getProfilePicture(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .toList()
                , user.getRoles().stream().map(role -> role.getName().name()).toList()
        );
    }

    public UserDto getUserSignedIn() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return (UserDto) authentication.getPrincipal();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ProfilePictureDto getUserProfilePicture(UserDto userDTO) throws IOException {
        if (userDTO.profilePicturePath() == null || userDTO.profilePicturePath().isEmpty())
            throw new ProfilePictureException("Foto de perfil não encontrada.", HttpStatus.NOT_FOUND);

        String uploadDir = "uploads/profile-pictures/";
        File file = new File(uploadDir + userDTO.profilePicturePath());

        if (!file.exists() || !file.isFile()) {
            throw new ProfilePictureException("Foto de perfil não encontrada.", HttpStatus.NOT_FOUND);
        }

        return new ProfilePictureDto(Files.readAllBytes(file.toPath()), "image/png", String.valueOf(file.length()));
    }

    public List<Rating> getUserRatings(UserDto userDTO) {
        var ratings = ratingRepository.findAll();

        return ratings.stream().filter(r -> r.getSender().getUserId().equals(userDTO.userId()))
                .toList();
    }

    public List<Ticket> getUserTickets(UserDto userDTO) {
        var tickets = ticketRepository.findAll();

        return tickets.stream().filter(r -> r.getSender().getUserId().equals(userDTO.userId()))
                .toList();
    }

    public List<Message> getUserMessages(UserDto userDTO) {
        var messages = messageRepository.findAll();

        return messages.stream().filter(r -> r.getSender().getUserId().equals(userDTO.userId()))
                .toList();
    }

    public UserDto updateUserById (long id, UpdateUserDTO updateUserRequest) {
        var user = userRepository.findById(id)
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        if (updateUserRequest.roles().length > 0) {
            if (updateUserRequest.roles().length > 3)
                throw new IllegalArgumentException("A user can have a maximum of 3 roles.");

            user.getRoles().clear();
            for (String role : updateUserRequest.roles()) {
                 Optional<Role> userRoles = roleRepository.findById(Long.valueOf(role));

                if (userRoles.isEmpty())
                    throw new IllegalArgumentException("Role " + role + " does not exist.");


                user.getRoles().add(userRoles.get());
            }
        }

        if (updateUserRequest.email() != null && !updateUserRequest.email().isEmpty() && !user.getEmail().equals(updateUserRequest.email())) {
            if (userRepository.existsByEmail(updateUserRequest.email()))
                throw new IllegalArgumentException("Email already exists.");

            user.setEmail(updateUserRequest.email());
        }

        updateDefaultUser(updateUserRequest, user);

        userRepository.save(user);
        return UserDto.fromUser(user);

    }

    private void updateDefaultUser(UpdateUserDTO updateUserRequest, User user) {
        if (updateUserRequest.name() != null && !updateUserRequest.name().isEmpty())
            user.setName(updateUserRequest.name());

        if (updateUserRequest.password() != null && !updateUserRequest.password().isEmpty()) {
            if (updateUserRequest.newPassword() == null || updateUserRequest.newPassword().isEmpty())
                throw new PasswordException("New password must be provided.", HttpStatus.BAD_REQUEST);

            if (!passwordEncoder.matches(updateUserRequest.password(), user.getPassword()))
                throw new PasswordException("Current password is incorrect.", HttpStatus.UNAUTHORIZED);

            if (updateUserRequest.newPassword().equals(updateUserRequest.password()))
                throw new PasswordException("New password must be different from the current password.", HttpStatus.BAD_REQUEST);



            user.setPassword(passwordEncoder.encode(updateUserRequest.newPassword()));
        }

        if (updateUserRequest.profilePicture() != null) {
            ProfilePictureExtensions.removeOldProfilePicture(user.getProfilePicture());
            user.setProfilePicture(ProfilePictureExtensions.saveProfilePicture(updateUserRequest.profilePicture()));
        }
    }

    public User updateUser(UpdateUserDTO updateUserRequest) {
        var userDto = getUserSignedIn();
        var user = userRepository.findById(userDto.userId())
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userDto.userId()));

        updateDefaultUser(updateUserRequest, user);

        return userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                             .orElse(null);
    }

    public UserPageDto list(int page, int pageSize) {
        Page<User> pageUser = userRepository.findAll(PageRequest.of(page, pageSize));
        List<UserDto> users = pageUser.get().map(UserDto::fromUser).toList();
        return new UserPageDto(users, pageUser.getTotalElements(), pageUser.getTotalPages());
    }

    public List<User> findByRolesName(UserRole role) {
        return userRepository.findByRolesName(role);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}