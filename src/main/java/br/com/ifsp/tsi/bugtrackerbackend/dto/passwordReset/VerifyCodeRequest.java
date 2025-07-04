package br.com.ifsp.tsi.bugtrackerbackend.dto.passwordReset;

public record VerifyCodeRequest(
        String email,
        String code
) {
}
