package br.com.ifsp.tsi.bugtrackerbackend.dto;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;

import java.time.LocalDateTime;

public record MessageDto (
        long messageId,
        String message,
        LocalDateTime timestamp,
        Ticket ticket,
        User sender
) { }