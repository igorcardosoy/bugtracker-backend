package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import br.com.ifsp.tsi.bugtrackerbackend.service.TicketImageDto;
import br.com.ifsp.tsi.bugtrackerbackend.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(
                ticketService.getAllTickets()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        var ticket = ticketService.getTicketById(id);

        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(TicketResponseDTO.fromTicket(ticket));
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<?> getTicketImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get("uploads/ticket-images/" + filename);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileBytes = Files.readAllBytes(path);
        String contentType = Files.probeContentType(path);
        long length = fileBytes.length;

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Length", String.valueOf(length))
                .body(fileBytes);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketResponseDTO> createTicket(
            @RequestPart("ticket") TicketRequestDTO ticketData,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(ticketData, images));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @RequestPart("ticket") TicketRequestDTO ticketData,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketData, images));
    }

    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<?> updateTicketStatus (
            @PathVariable("ticketId") long ticketId,
            @RequestParam TicketStatus status
    ) {
        ticketService.updateTicketStatus(ticketId, status);

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDTO> deleteTicket (
            @PathVariable Long ticketId
    ) {
        ticketService.deleteTicket(ticketId);

        return ResponseEntity.noContent().build();
    }
}