package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.CodeVerificationResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.JwtResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.LoginRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.RegisterRequest;
import br.com.ifsp.tsi.bugtrackerbackend.exception.*;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.PasswordResetCode;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.PasswordResetCodeRepository;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RoleRepository;
import br.com.ifsp.tsi.bugtrackerbackend.repository.UserRepository;
import br.com.ifsp.tsi.bugtrackerbackend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@Log4j2
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordResetCodeRepository passwordResetCodeRepository;

    public AuthService(
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            EmailService emailService,
            PasswordResetCodeRepository passwordResetCodeRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordResetCodeRepository = passwordResetCodeRepository;
    }

    public void verifyExistence(String email) {
        if (userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("Email is already in use!", HttpStatus.BAD_REQUEST);
    }

    public void register(RegisterRequest registerRequest) {
        verifyExistence(registerRequest.email());

        String randomInitialPassword = generateRamdomCode();

        Set<Role> roles = new HashSet<>();
        for (Role userRole : registerRequest.userRoles()) {
            Optional<Role> role = roleRepository.findByName(userRole.getName());
            if (role.isEmpty()) {
                throw new RoleNotFoundException("Role " + userRole + " is not found.", HttpStatus.NOT_FOUND);
            }
            roles.add(role.get());
        }

        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(randomInitialPassword));
        user.setRoles(roles);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), randomInitialPassword);

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

    public void sendResetCode(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email, HttpStatus.NOT_FOUND));

        String code = generateRamdomCode();

        PasswordResetCode resetCode = new PasswordResetCode(email, code);
        passwordResetCodeRepository.save(resetCode);

        emailService.sendPasswordResetEmail(email, code);

        log.info("Password reset code sent to: {}", email);
    }

    private static String generateRamdomCode() {
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000));
        return code;
    }

    public CodeVerificationResponse verifyResetCode(String email, String code) {
        PasswordResetCode resetCode = passwordResetCodeRepository.findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new CodeException("Invalid or expired code", HttpStatus.BAD_REQUEST));

        if (resetCode.isExpired()) {
            throw new CodeException("Code has expired", HttpStatus.BAD_REQUEST);
        }

        resetCode.setUsed(true);
        passwordResetCodeRepository.save(resetCode);

        String resetToken = jwtUtil.generateResetToken(email);

        log.info("Reset code verified for: {}", email);
        return new CodeVerificationResponse(resetToken);
    }


    @Transactional
    public void resetPasswordWithToken(String email, String newPassword, String token) {
        if (!jwtUtil.validateResetToken(token, email)) {
            throw new TokenException("Invalid or expired reset token", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetCodeRepository.deleteByEmailAndUsedTrue(email);

        log.info("Password reset successfully for: {}", email);
    }


    @Transactional
    @Scheduled(fixedRate = 300000) //
    public void cleanupExpiredCodes() {
        passwordResetCodeRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }


}
