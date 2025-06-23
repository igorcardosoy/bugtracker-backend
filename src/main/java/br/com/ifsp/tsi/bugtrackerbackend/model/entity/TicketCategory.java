package br.com.ifsp.tsi.bugtrackerbackend.model.entity;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketCategoryDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_categories")
@Data
@NoArgsConstructor
public class TicketCategory {
    @Id
    @Column(name = "ticket_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketCategoryId;

    private String description;
    private boolean isActive;

    public TicketCategory(TicketCategoryDto request) {
        this.ticketCategoryId = request.id();
        this.description = request.description();
        this.isActive = request.isActive();
    }
}