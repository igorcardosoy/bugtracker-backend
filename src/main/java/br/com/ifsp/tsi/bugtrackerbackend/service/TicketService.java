package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
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
        var ticket = new Ticket(request);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(long ticketId, TicketStatus status) {
        var ticket = getTicketById(ticketId);

        ticket.setTicketStatus(status);

        return ticketRepository.save(ticket);
    }

    public Ticket deleteTicket(Long ticketId) {
        var ticket = getTicketById(ticketId);

        ticketRepository.delete(ticket);

        return ticket;
    }
}