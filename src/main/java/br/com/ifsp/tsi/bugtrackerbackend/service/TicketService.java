package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketDto;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final UserService userService;
    private final TicketCategoryService ticketCategoryService;
    private final TicketRepository ticketRepository;

    public TicketService(
            UserService userService,
            TicketCategoryService ticketCategoryService,
            TicketRepository ticketRepository
    ) {
        this.userService = userService;
        this.ticketCategoryService = ticketCategoryService;
        this.ticketRepository = ticketRepository;
    }

    public Ticket getTicketById(Long id) {
        var optional = ticketRepository.findById(id);

        return optional.orElseGet(Ticket::new);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(TicketDto request) {
        var userDto = userService.getUserSignedIn();
        var user = new User(userDto);

        var category = ticketCategoryService.getCategoryById(request.categoryId());

        var ticket = new Ticket(request, user, category);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(long ticketId, TicketStatus status) {
        var ticket = getTicketById(ticketId);

        ticket.setTicketStatus(status);

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long ticketId) {
        var ticket = getTicketById(ticketId);

        if (ticket.getTicketId() != ticketId)
            throw new TicketNotFoundException("Ticket not found", HttpStatus.NOT_FOUND);

        ticketRepository.delete(ticket);
    }
}