package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import lombok.Data;

import java.util.Set;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String profilePicture;
    private Set<Role> roles;
}
