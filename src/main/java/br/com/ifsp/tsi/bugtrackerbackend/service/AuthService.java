package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.JwtResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.LoginRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.RegisterRequest;
import br.com.ifsp.tsi.bugtrackerbackend.exception.RoleNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.exception.UserAlreadyExistsException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.UserRole;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RoleRepository;
import br.com.ifsp.tsi.bugtrackerbackend.repository.UserRepository;
import br.com.ifsp.tsi.bugtrackerbackend.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void verifyExistence(String email) {
        if (userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("Email is already in use!", HttpStatus.BAD_REQUEST);
    }

    public void register(RegisterRequest registerRequest) {
        verifyExistence(registerRequest.email());

        String encodedPassword = passwordEncoder.encode(registerRequest.password());

        String profilePicturePath = null;
        if (registerRequest.profilePicture() != null && !registerRequest.profilePicture().isEmpty()) {
            profilePicturePath = ProfilePictureExtensions.saveProfilePicture(registerRequest.profilePicture());
        }

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(UserRole.ROLE_USER);

        if (userRole.isEmpty()) throw new RoleNotFoundException("Role is not found.", HttpStatus.NOT_FOUND);

        roles.add(userRole.get());

        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(encodedPassword);
        user.setProfilePicture(profilePicturePath);
        user.setRoles(roles);
        userRepository.save(user);

        log.info("User registered successfully: {}", user.getEmail());
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User authenticated successfully: {}", loginRequest.email());
        return jwtUtil.createJwtResponse((UserDto) authentication.getPrincipal());
    }
}
