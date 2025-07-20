package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ticketCategory.TicketCategoryRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.ticketCategory.TicketCategoryResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.service.TicketCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugtracker/categories")
@PreAuthorize("hasRole('USER') or hasRole('TECHNICIAN') or hasRole('ADMIN')")
public class TicketCategoryController {
    private final TicketCategoryService ticketCategoryService;

    public TicketCategoryController(TicketCategoryService ticketCategoryService){
        this.ticketCategoryService = ticketCategoryService;
    }

    @GetMapping()
    public ResponseEntity<List<TicketCategoryResponseDTO>> getCategories() {
        var categories = ticketCategoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        var category = ticketCategoryService.getCategoryById(id);

        return ResponseEntity.ok(TicketCategoryResponseDTO.fromTicketCategory(category));
    }

    @PostMapping()
    public ResponseEntity<TicketCategoryResponseDTO> createCategory(
            @RequestBody TicketCategoryRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ticketCategoryService.createTicketCategory(request)
        );
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody TicketCategoryRequestDTO request
    ) {
        ticketCategoryService.updateCategory(
                categoryId,
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long categoryId
    ) {
        ticketCategoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}