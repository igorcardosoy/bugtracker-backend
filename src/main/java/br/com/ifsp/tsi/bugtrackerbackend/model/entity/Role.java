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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private UserRole name;
}
