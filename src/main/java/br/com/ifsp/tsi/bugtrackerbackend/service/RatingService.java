package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.RatingDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Rating updateRating(long ratingId, float ratingValue) {
        var optional = ratingRepository.findById(ratingId);

        if (optional.isEmpty())
            return new Rating();

        var rating = optional.get();

        rating.setRatingValue(ratingValue);
        ratingRepository.save(rating);

        return rating;
    }

    public List<Rating> getAllRatingsByTicket(long ticketId) {
        return ratingRepository.findAll().stream().filter(
                rating -> rating.getTicket().getTicketId() == ticketId)
                .toList();
    }

    public Rating createRating(long ticketId, RatingDto request) {
        var userDto = userService.getUserSignedIn();
        var ticket = ticketService.getTicketById(ticketId);

        var user = new User(userDto);
        var rating = new Rating(request, user, ticket);

        return ratingRepository.save(rating);
    }
}