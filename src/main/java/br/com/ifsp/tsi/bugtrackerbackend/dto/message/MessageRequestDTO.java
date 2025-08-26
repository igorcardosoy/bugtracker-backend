package br.com.ifsp.tsi.bugtrackerbackend.dto.message;

import java.time.LocalDateTime;

public record MessageRequestDTO(
        long senderId,
        String message,
        LocalDateTime timestamp
) { }