package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ticketCategory.TicketCategoryRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.ticketCategory.TicketCategoryResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketCategoryNotFoundException;
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

    public List<TicketCategoryResponseDTO> getAllCategories() {
        var ticketCategories = ticketCategoryRepository.findAll();

        return BuildTicketCategoryResponseDTOs(ticketCategories);
    }

    private ArrayList<TicketCategoryResponseDTO> BuildTicketCategoryResponseDTOs(List<TicketCategory> ticketCategories) {
        var ticketCategoryResponseDTOs = new ArrayList<TicketCategoryResponseDTO>();

        for (TicketCategory ticketCategory : ticketCategories)
            ticketCategoryResponseDTOs.add(TicketCategoryResponseDTO.fromTicketCategory(ticketCategory));

        return ticketCategoryResponseDTOs;
    }

    public TicketCategoryResponseDTO createTicketCategory(TicketCategoryRequestDTO request) {
        var ticketCategory = new TicketCategory(request);

        var savedTicketCategory = ticketCategoryRepository.save(ticketCategory);

        return TicketCategoryResponseDTO.fromTicketCategory(savedTicketCategory);
    }

    public void updateCategory(long ticketCategoryId, TicketCategoryRequestDTO request) {
        if (ticketCategoryRepository.findById(ticketCategoryId).isEmpty())
            throw new TicketCategoryNotFoundException();

        var ticketCategory = new TicketCategory(request);

        ticketCategory.setTicketCategoryId(ticketCategoryId);

        ticketCategoryRepository.save(ticketCategory);
    }

    public void deleteCategory(long ticketCategoryId) {
        var ticketCategory = ticketCategoryRepository.findById(ticketCategoryId);

        if (ticketCategory.isEmpty())
            throw new TicketCategoryNotFoundException();

        ticketCategoryRepository.deleteById(ticketCategoryId);
    }

    public TicketCategory getCategoryById(long categoryId) {
        return ticketCategoryRepository.findById(categoryId)
                                       .orElse(null);
    }
}