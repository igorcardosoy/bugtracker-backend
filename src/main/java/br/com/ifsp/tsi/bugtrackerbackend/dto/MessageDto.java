package br.com.ifsp.tsi.bugtrackerbackend.dto;

import java.time.LocalDateTime;

public record MessageDto (
        String message,
        LocalDateTime timestamp,
        long senderId
) { }