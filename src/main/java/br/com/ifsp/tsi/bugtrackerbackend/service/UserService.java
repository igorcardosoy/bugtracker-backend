package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ProfilePictureDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.LoginRequest;
import br.com.ifsp.tsi.bugtrackerbackend.exception.ProfilePictureException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

        return new UserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfilePicture(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .toList()
        );
    }

    public UserDto getUserSignedIn() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return (UserDto) authentication.getPrincipal();
    }

    public ProfilePictureDto getUserProfilePicture(UserDto userDTO) throws IOException {
        if (userDTO.profilePicture() == null || userDTO.profilePicture().isEmpty())
            throw new ProfilePictureException("Foto de perfil não encontrada.", HttpStatus.NOT_FOUND);


        String uploadDir = "uploads/profile-pictures/";
        File file = new File(uploadDir + userDTO.profilePicture());

        return new ProfilePictureDto(Files.readAllBytes(file.toPath()), "image/png", String.valueOf(file.length()));
    }

    public List<Rating> getUserRatings(UserDto userDTO) {
        return null;
    }

    public List<Ticket> getUserTickets(UserDto userDTO) {
        return null;
    }

    public List<Message> getUserMessages(UserDto userDTO) {
        return null;
    }

    public User updateUser(LoginRequest updateUserRequest) {
        return null;
    }
}