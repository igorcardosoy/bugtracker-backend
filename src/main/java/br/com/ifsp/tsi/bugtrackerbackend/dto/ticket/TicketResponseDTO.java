package br.com.ifsp.tsi.bugtrackerbackend.dto.ticket;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public record TicketResponseDTO(
    long ticketId,
    long senderId,
    long receiverId,
    long ticketCategoryId,
    long ratingId,
    String title,
    String description,
    String ticketStatus,
    LocalDateTime timestamp,
    List<String> imagesAttachedPaths
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
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getTicketStatus().name(),
                ticket.getTimestamp(),
                ticket.getImagesAttachedPaths()
        );
    }
}