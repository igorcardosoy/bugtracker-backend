package br.com.ifsp.tsi.bugtrackerbackend.dto.auth;

public record RegisterRequest(
    String name,
    String email,
    String password
) {
}
