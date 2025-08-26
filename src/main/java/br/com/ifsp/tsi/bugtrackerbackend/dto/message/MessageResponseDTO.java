package br.com.ifsp.tsi.bugtrackerbackend.dto.message;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        long messageId,
        User sender,
        long ticketId,
        String message,
        LocalDateTime timestamp,
        boolean wasEdited
){
    public static MessageResponseDTO fromMessage(Message message) {
        return new MessageResponseDTO(
                message.getMessageId(),
                message.getSender(),
                message.getTicket().getTicketId(),
                message.getMessage(),
                message.getTimestamp(),
                message.isWasEdited()
        );
    }
}