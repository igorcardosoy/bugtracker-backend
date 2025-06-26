package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.MessageDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugtracker/messages")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<List<Message>> getAllMessagesByTicket (
            @PathVariable("ticketId") long ticketId
    ) {
        return ResponseEntity.ok(
                messageService.getAllMessagesByTicket(ticketId)
        );
    }

    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<Message> createTicketMessage (
            @PathVariable("ticketId") long ticketId,
            @RequestBody MessageDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                messageService.createTicketMessage(ticketId, request)
        );
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage (
            @PathVariable("messageId") long messageId,
            @RequestParam String message
    ) {
        return ResponseEntity.ok(
                messageService.updateMessage(messageId, message)
        );
    }
}