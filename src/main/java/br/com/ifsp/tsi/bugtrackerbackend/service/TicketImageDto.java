package br.com.ifsp.tsi.bugtrackerbackend.service;

public record TicketImageDto(
    byte[] imageData,
    String contentType,
    String length
) { }
