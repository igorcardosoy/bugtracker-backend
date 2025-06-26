package br.com.ifsp.tsi.bugtrackerbackend.dto;

public record TicketCategoryDto(
        long id,
        String description,
        boolean isActive
) { }