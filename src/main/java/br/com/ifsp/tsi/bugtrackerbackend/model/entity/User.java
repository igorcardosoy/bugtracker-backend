package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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

    @OneToMany(mappedBy = "user")
//    @JoinColumn(name = "ticket_id")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "sender")
//    @JoinColumn(name = "message_id")
    private List<Message> messages;

    //@OneToMany(targetEntity = Rating.class)
    @OneToMany(mappedBy = "sender")
//    @JoinColumn(name = "rating_id")
    private List<Rating> ratings;

    public User(UserDto request) {
        this.userId = request.id();
        this.email = request.email();
        this.name = request.name();
        this.password = request.password();
        this.profilePicture = request.profilePicture();
    }
}