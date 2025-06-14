package br.com.ifsp.tsi.bugtrackerbackend.dto.auth;

public record LoginRequest(
    String email,
    String password
) {
}
