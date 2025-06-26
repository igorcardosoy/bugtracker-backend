package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketCategoryDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.TicketCategory;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketCategoryService {
    private final TicketCategoryRepository ticketCategoryRepository;

    public TicketCategoryService(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    public List<TicketCategory> getAllCategories() {
        return new ArrayList<>(
                ticketCategoryRepository.findAll()
        );
    }

    public TicketCategory createTicketCategory(TicketCategoryDto request) {
        var ticketCategory = new TicketCategory(request);

        return ticketCategoryRepository.save(ticketCategory);
    }

    public TicketCategory updateCategory(TicketCategoryDto request) {
        var ticketCategory = new TicketCategory(request);

        return ticketCategoryRepository.save(ticketCategory);
    }

    public TicketCategory deleteCategory(Long categoryId) {
        var ticketCategory = ticketCategoryRepository.findById(categoryId);

        ticketCategoryRepository.deleteById(categoryId);

        return ticketCategory.orElseGet(TicketCategory::new);
    }
}