package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.rating.RatingResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketRatingNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RatingRepository;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private final UserService userService;
    private final TicketService ticketService;

    private final RatingRepository ratingRepository;

    public RatingService(
            UserService userService,
            TicketService ticketService,
            RatingRepository ratingRepository
    ) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.ratingRepository = ratingRepository;
    }

    public RatingResponseDTO getRatingByTicketId(long ticketId) {
        var ratingOptional = ratingRepository.findAll()
                .stream()
                .filter(rating -> rating.getTicket().getTicketId() == ticketId)
                .findFirst();

        if (ratingOptional.isEmpty())
            throw new TicketRatingNotFoundException();

        return RatingResponseDTO.fromRating(ratingOptional.get());
    }

    public RatingResponseDTO createRating(long ticketId,  float ratingValue) {
        var userDto = userService.getUserSignedIn();
        var ticket = ticketService.getTicketById(ticketId);

        var user = new User(userDto);
        var rating = new Rating(ratingValue, user, ticket);

        var savedRating = ratingRepository.save(rating);

        return RatingResponseDTO.fromRating(savedRating);
    }

    public void updateRating(long ratingId, float ratingValue) {
        var optional = ratingRepository.findById(ratingId);

        if (optional.isEmpty())
            throw new TicketRatingNotFoundException();

        var rating = optional.get();

        rating.setRatingValue(ratingValue);

        ratingRepository.save(rating);
    }
}