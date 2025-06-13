package br.com.ifsp.tsi.bugtrackerbackend.dto.auth;

import java.util.List;

public record JwtResponse(
        String token,
        Long id,
        String username,
        List<String> roles
) { }
