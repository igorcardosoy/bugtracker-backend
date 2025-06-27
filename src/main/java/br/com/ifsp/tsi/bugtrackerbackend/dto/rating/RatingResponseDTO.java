package br.com.ifsp.tsi.bugtrackerbackend.dto.rating;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;

public record RatingResponseDTO (
        long ratingId,
        long ticketId,
        long senderId,
        float ratingValue
) {
    public static RatingResponseDTO fromRating(Rating rating) {
        return new RatingResponseDTO(
                rating.getRatingId(),
                rating.getTicket().getTicketId(),
                rating.getSender().getUserId(),
                rating.getRatingValue()
        );
    }
}