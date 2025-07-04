package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.CodeVerificationResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.ForgotPasswordRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.ResetPasswordRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.VerifyCodeRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.JwtResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.LoginRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.RegisterRequest;
import br.com.ifsp.tsi.bugtrackerbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bugtracker/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {

        authService.register(new RegisterRequest(name, email, password, profilePicture));
        return ResponseEntity.status(HttpStatus.CREATED).body("Success: User registered");
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendResetCode(request.email());
        return ResponseEntity.ok("Reset code sent to your email");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<CodeVerificationResponse> verifyCode(@RequestBody VerifyCodeRequest request) {
        CodeVerificationResponse response = authService.verifyResetCode(request.email(), request.code());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPasswordWithToken(request.email(), request.newPassword(), request.token());
        return ResponseEntity.ok("Password reset successfully");
    }
}