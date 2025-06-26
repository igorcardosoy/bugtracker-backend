package br.com.ifsp.tsi.bugtrackerbackend.dto;

import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketDto(
        long senderId,
        long categoryId,
        String description,
        TicketStatus ticketStatus,
        LocalDateTime timestamp
) { }