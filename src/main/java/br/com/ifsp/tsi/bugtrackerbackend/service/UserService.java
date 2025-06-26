package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ProfilePictureDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UpdateUserDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.exception.ProfilePictureException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.MessageRepository;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RatingRepository;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketRepository;
import br.com.ifsp.tsi.bugtrackerbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final TicketRepository ticketRepository;
    private final MessageRepository messageRepository;

    public UserService(
            UserRepository userRepository,
            RatingRepository ratingRepository,
            TicketRepository ticketRepository,
            MessageRepository messageRepository
    ) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with username: " + email));

        return new UserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfilePicture(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .toList()
        );
    }

    public UserDto getUserSignedIn() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return (UserDto) authentication.getPrincipal();
    }

    public ProfilePictureDto getUserProfilePicture(UserDto userDTO) throws IOException {
        if (userDTO.profilePicturePath() == null || userDTO.profilePicturePath().isEmpty())
            throw new ProfilePictureException("Foto de perfil não encontrada.", HttpStatus.NOT_FOUND);


        String uploadDir = "uploads/profile_pictures/";
        File file = new File(uploadDir + userDTO.profilePicturePath());

        return new ProfilePictureDto(Files.readAllBytes(file.toPath()), "image/png", String.valueOf(file.length()));
    }

    public List<Rating> getUserRatings(UserDto userDTO) {
        var ratings = ratingRepository.findAll();

        return ratings.stream().filter(r -> r.getSender().getUserId().equals(userDTO.id()))
                .toList();
    }

    public List<Ticket> getUserTickets(UserDto userDTO) {
        var tickets = ticketRepository.findAll();

        return tickets.stream().filter(r -> r.getSender().getUserId().equals(userDTO.id()))
                .toList();
    }

    public List<Message> getUserMessages(UserDto userDTO) {
        var messages = messageRepository.findAll();

        return messages.stream().filter(r -> r.getSender().getUserId().equals(userDTO.id()))
                .toList();
    }

    public User updateUser(UpdateUserDTO updateUserRequest) {
        var userDto = getUserSignedIn();
        var user = new User(userDto);

        if (!updateUserRequest.profilePicture().isEmpty())
            user.setProfilePicture(
                    ProfilePictureExtensions.saveProfilePicture(updateUserRequest.profilePicture())
            );

        return userRepository.save(user);
    }
}