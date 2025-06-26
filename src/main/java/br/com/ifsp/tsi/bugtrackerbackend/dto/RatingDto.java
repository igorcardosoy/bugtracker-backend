package br.com.ifsp.tsi.bugtrackerbackend.dto;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;

public record RatingDto(
        long ratingId,
        Ticket ticket,
        User sender,
        Float ratingValue
) { }