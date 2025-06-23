package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.dto.MessageDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
public class Message {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;

    private String message;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private boolean wasEdited;

    public Message(MessageDto request, User sender, Ticket ticket) {
        this.message = request.message();
        this.ticket = ticket;
        this.sender = sender;
    }
}