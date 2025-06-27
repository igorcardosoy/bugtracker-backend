package br.com.ifsp.tsi.bugtrackerbackend.dto.ticketCategory;

public record TicketCategoryRequestDTO(
        String description,
        boolean isActive
) { }