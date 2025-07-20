package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private UserRole name;
}