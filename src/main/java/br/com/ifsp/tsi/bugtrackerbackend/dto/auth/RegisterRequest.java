package br.com.ifsp.tsi.bugtrackerbackend.dto.auth;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;

import java.util.List;

public record RegisterRequest(
    String name,
    String email,
    List<Role> userRoles
) {
}
