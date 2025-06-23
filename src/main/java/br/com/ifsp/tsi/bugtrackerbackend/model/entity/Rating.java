package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.dto.RatingDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
public class Rating {
    @Id
    @Column(name = "rating_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ratingId;

    @OneToOne()
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne()
    @JoinColumn(name = "sender_id")
    private User sender;

    private float ratingVlue;

    public Rating(RatingDto request, User user, Ticket ticket) {
        this.ticket = ticket;
        this.sender = user;
        this.ratingVlue = request.ratingValue();
    }

    public void setRatingValue(float ratingValue) {
        this.ratingVlue = ratingValue;
    }
}