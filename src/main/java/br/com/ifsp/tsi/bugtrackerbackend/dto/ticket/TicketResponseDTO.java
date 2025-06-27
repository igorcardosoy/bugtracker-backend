package br.com.ifsp.tsi.bugtrackerbackend.dto.ticket;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;

import java.time.LocalDateTime;

public record TicketResponseDTO(
    long ticketId,
    long senderId,
    long receiverId,
    long ticketCategoryId,
    long ratingId,
    String description,
    String ticketStatus,
    LocalDateTime timestamp
) {
    public static TicketResponseDTO fromTicket(Ticket ticket) {
        var receiver = ticket.getReceiver();
        long receiverId = 0;

        if (receiver != null)
            receiverId = receiver.getUserId();

        var rating = ticket.getRating();
        long ratingId = 0;

        if (rating != null)
            ratingId = rating.getRatingId();

        return new TicketResponseDTO(
                ticket.getTicketId(),
                ticket.getSender().getUserId(),
                receiverId,
                ticket.getTicketCategory().getTicketCategoryId(),
                ratingId,
                ticket.getDescription(),
                ticket.getTicketStatus().name(),
                ticket.getTimestamp()
        );
    }
}