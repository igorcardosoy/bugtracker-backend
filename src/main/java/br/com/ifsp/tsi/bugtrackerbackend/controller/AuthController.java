package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.CodeVerificationResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.ForgotPasswordRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.ResetPasswordRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset.VerifyCodeRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.JwtResponse;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.LoginRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.RegisterRequest;
import br.com.ifsp.tsi.bugtrackerbackend.dto.role.RoleResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.RoleNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RoleRepository;
import br.com.ifsp.tsi.bugtrackerbackend.service.AuthService;
import br.com.ifsp.tsi.bugtrackerbackend.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bugtracker/auth")
public class AuthController {

    private final AuthService authService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public AuthController(AuthService authService, RoleService roleService, RoleRepository roleRepository) {
        this.authService = authService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("userRoles") List<Long> roleIds
    ) {
        List<Role> userRoles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RoleNotFoundException("Role with id " + roleId + " not found", HttpStatus.NOT_FOUND)))
                .collect(Collectors.toList());
        authService.register(new RegisterRequest(name, email, userRoles));
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
}