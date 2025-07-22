package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.message.MessageRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.message.MessageResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugtracker/messages")
@PreAuthorize("hasRole('USER') or hasRole('TECHNICIAN') or hasRole('ADMIN')")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<List<MessageResponseDTO>> getAllMessagesByTicket (
            @PathVariable("ticketId") long ticketId
    ) {
        return ResponseEntity.ok(
                messageService.getAllMessagesByTicket(ticketId)
        );
    }

    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<MessageResponseDTO> createTicketMessage (
            @PathVariable("ticketId") long ticketId,
            @RequestBody MessageRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                messageService.createTicketMessage(ticketId, request)
        );
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<?> updateMessage (
            @PathVariable("messageId") long messageId,
            @RequestBody String message
    ) {
        messageService.updateMessage(messageId, message);

        return ResponseEntity.noContent().build();
    }
}