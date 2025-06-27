package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.rating.RatingResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bugtracker/ratings")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<RatingResponseDTO> getTicketRating (
            @PathVariable("ticketId") long ticketId
    ) {
        return ResponseEntity.ok(
                ratingService.getRatingByTicketId(ticketId)
        );
    }

    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<RatingResponseDTO> createTicketRating (
            @PathVariable("ticketId") long ticketId,
            @RequestParam float ratingValue
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ratingService.createRating(ticketId, ratingValue)
        );
    }

    @PatchMapping("/{ratingId}")
    public ResponseEntity<RatingResponseDTO> updateRatingValue (
            @PathVariable("ratingId") long ratingId,
            @RequestParam float ratingValue
    ) {
        ratingService.updateRating(ratingId, ratingValue);

        return ResponseEntity.noContent().build();
    }
}