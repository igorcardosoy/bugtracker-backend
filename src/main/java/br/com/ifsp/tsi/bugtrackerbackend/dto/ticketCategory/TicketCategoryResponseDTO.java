package br.com.ifsp.tsi.bugtrackerbackend.dto.ticketCategory;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.TicketCategory;

public record TicketCategoryResponseDTO(
        long ticketCategoryId,
        String description,
        boolean isActive
) {
    public static TicketCategoryResponseDTO fromTicketCategory(TicketCategory ticketCategory) {
        return new TicketCategoryResponseDTO(
                ticketCategory.getTicketCategoryId(),
                ticketCategory.getDescription(),
                ticketCategory.isActive()
        );
    }
}