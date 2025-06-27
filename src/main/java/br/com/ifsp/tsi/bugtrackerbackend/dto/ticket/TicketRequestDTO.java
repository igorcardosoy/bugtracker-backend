package br.com.ifsp.tsi.bugtrackerbackend.dto.ticket;

import java.time.LocalDateTime;

public record TicketRequestDTO(
        long senderId,
        long receiverId,
        long ticketCategoryId,
        long ratingId,
        String description,
        String ticketStatus,
        LocalDateTime timestamp
) { }