package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketCategoryNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        return optional.orElse(null);
    }

    public List<TicketResponseDTO> getAllTickets() {
        var savedTickets =  ticketRepository.findAll();

        return BuildTicketResponseDTOs(savedTickets);
    }

    private ArrayList<TicketResponseDTO> BuildTicketResponseDTOs(List<Ticket> savedTickets) {
        var tickets = new ArrayList<TicketResponseDTO>();

        for (var ticket : savedTickets)
            tickets.add(TicketResponseDTO.fromTicket(ticket));

        return tickets;
    }

    public TicketResponseDTO createTicket(TicketRequestDTO request) {
        var userDto = userService.getUserSignedIn();
        var sender = new User(userDto);

        var receiver = userService.getUserById(request.senderId());

        var category = ticketCategoryService.getCategoryById(request.ticketCategoryId());

        if (category == null)
            throw new TicketCategoryNotFoundException();

        var ticket = new Ticket(
                request,
                sender,
                receiver,
                category);

        var savedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromTicket(savedTicket);
    }

    public void updateTicketStatus(long ticketId, TicketStatus status) {
        var ticket = getTicketById(ticketId);

        ticket.setTicketStatus(status);

        ticketRepository.save(ticket);
    }

    public void deleteTicket(Long ticketId) {
        var ticket = getTicketById(ticketId);

        if (ticket == null)
            throw new TicketNotFoundException();

        ticketRepository.delete(ticket);
    }
}