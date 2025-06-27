package br.com.ifsp.tsi.bugtrackerbackend.dto;

public record TicketCategoryDto(
        long ticketCategoryId,
        String description,
        boolean isActive
) { }