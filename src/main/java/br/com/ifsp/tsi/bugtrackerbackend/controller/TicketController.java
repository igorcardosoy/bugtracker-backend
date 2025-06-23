package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import br.com.ifsp.tsi.bugtrackerbackend.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugtracker/tickets")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping()
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(
                ticketService.getAllTickets()
        );
    }

    @PostMapping()
    public ResponseEntity<Ticket> createTicket(
            @RequestBody TicketDto request
    ) {
        return ResponseEntity.ok(
                ticketService.createTicket(request)
        );
    }

    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus (
            @PathVariable("ticketId") long ticketId,
            @RequestBody TicketStatus status
    ) {
        return ResponseEntity.ok(
                ticketService. updateTicketStatus(ticketId, status)
        );
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Ticket> deleteTicket (
            @PathVariable Long ticketId
    ) {
        return ResponseEntity.ok(
                ticketService.deleteTicket(ticketId)
        );
    }
}
