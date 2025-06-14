package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    private String name;
    private String password;
    private String profilePicture;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
