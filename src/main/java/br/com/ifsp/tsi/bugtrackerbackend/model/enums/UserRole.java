package br.com.ifsp.tsi.bugtrackerbackend.model.enums;

public enum UserRole {
    ROLE_USER("Usuário Comum"),
    ROLE_TECHNICIAN("Técnico"),
    ROLE_ADMIN("Administrador");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
