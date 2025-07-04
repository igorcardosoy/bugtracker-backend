package br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset;

public record ResetPasswordRequest(
        String email, String newPassword, String token
) {
}
