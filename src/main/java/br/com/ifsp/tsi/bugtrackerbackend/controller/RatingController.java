package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugtracker/ratings")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<List<Rating>> getAllTicketRatings (
            @PathVariable("ticketId") long ticketId
    ) {
        return ResponseEntity.ok(
                ratingService.getAllRatingsByTicket(ticketId)
        );
    }

    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<Rating> createTicketRating (
            @PathVariable("ticketId") long ticketId,
            @RequestParam float ratingValue
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ratingService.createRating(ticketId, ratingValue)
        );
    }

    @PatchMapping("/{ratingId}")
    public ResponseEntity<Rating> updateRatingValue (
            @PathVariable("ratingId") long ratingId,
            @RequestParam float ratingValue
    ) {
        return ResponseEntity.ok(
                ratingService.updateRating(ratingId, ratingValue)
        );
    }
}