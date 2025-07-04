package br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset;

public record CodeVerificationResponse(
        String resetToken
) {
}
