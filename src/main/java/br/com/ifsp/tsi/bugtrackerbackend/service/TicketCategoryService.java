package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketCategoryDto;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketCategoryNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.TicketCategory;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketCategoryService {
    private final TicketCategoryRepository ticketCategoryRepository;

    public TicketCategoryService(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    public List<TicketCategory> getAllCategories() {
        return ticketCategoryRepository.findAll();
    }

    public TicketCategory createTicketCategory(TicketCategoryDto request) {
        var ticketCategory = new TicketCategory(request);

        return ticketCategoryRepository.save(ticketCategory);
    }

    public TicketCategory updateCategory(TicketCategoryDto request) {
        if (ticketCategoryRepository.findById(request.ticketCategoryId()).isEmpty())
            throw new TicketCategoryNotFoundException("Category not found", HttpStatus.NOT_FOUND);

        var ticketCategory = new TicketCategory(request);

        return ticketCategoryRepository.save(ticketCategory);
    }

    public void deleteCategory(Long categoryId) {
        var ticketCategory = ticketCategoryRepository.findById(categoryId);

        if (ticketCategory.isEmpty()) {
            throw new TicketCategoryNotFoundException("Category not found", HttpStatus.NOT_FOUND);
        }

        ticketCategoryRepository.deleteById(categoryId);
    }

    public TicketCategory getCategoryById(long categoryId) {
        return ticketCategoryRepository.findById(categoryId)
                                       .orElse(null);
    }
}