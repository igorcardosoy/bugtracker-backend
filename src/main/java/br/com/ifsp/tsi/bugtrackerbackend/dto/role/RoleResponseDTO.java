package br.com.ifsp.tsi.bugtrackerbackend.dto.role;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Role;

public record RoleResponseDTO(
        Long roleId,
        String name
) {
    public static RoleResponseDTO fromRole(Role role) {
        return new RoleResponseDTO(
            role.getRoleId(),
            role.getName().getDescription()
        );
    }
}
