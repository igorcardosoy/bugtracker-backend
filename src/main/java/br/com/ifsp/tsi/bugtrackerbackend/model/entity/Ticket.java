package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class Ticket {
    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    @OneToOne()
    @JoinColumn(name = "ticket_category_id")
    private TicketCategory ticketCategory;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @OneToMany(mappedBy = "ticket")
    @JsonBackReference
    private List<Message> messages;

    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonBackReference
    private User sender;

    @ManyToOne
    @JoinColumn(name = "reicever_id")
    private User reicever;

    private LocalDateTime lastUpdate;

    public Ticket(TicketDto request, User user, TicketCategory category) {
        this.user = user;
        this.sender = user;
        this.ticketCategory = category;
        this.description = request.description();
        this.ticketStatus = request.ticketStatus();
        this.timestamp = request.timestamp();
    }
}