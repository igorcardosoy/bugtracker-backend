package br.com.ifsp.tsi.bugtrackerbackend.dto;

import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketDto(
        long ticketId,
        String description,
        TicketStatus ticketStatus,
        LocalDateTime lastUpdate
) { }