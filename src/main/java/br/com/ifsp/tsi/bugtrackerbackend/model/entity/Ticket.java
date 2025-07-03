package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketRequestDTO;
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

    @ManyToOne
    @JoinColumn(name = "ticket_category_id", nullable = false)
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
    private User receiver;

    private LocalDateTime lastUpdate;

    public Ticket(TicketRequestDTO request, User user, User receiver, TicketCategory category) {
        this.user = user;
        this.sender = user;
        this.receiver = receiver;
        this.ticketCategory = category;
        this.description = request.description();
        this.ticketStatus = TicketStatus.valueOf(request.ticketStatus());
        this.timestamp = request.timestamp();
    }
}