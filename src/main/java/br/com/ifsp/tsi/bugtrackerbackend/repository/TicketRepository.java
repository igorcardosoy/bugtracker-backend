package br.com.ifsp.tsi.bugtrackerbackend.repository;

import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
