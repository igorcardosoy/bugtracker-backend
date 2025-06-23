package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketCategoryDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.TicketCategory;
import br.com.ifsp.tsi.bugtrackerbackend.service.TicketCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugtracker/categories")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class TicketCategoryController {
    private final TicketCategoryService ticketCategoryService;

    public TicketCategoryController(TicketCategoryService ticketCategoryService){
        this.ticketCategoryService = ticketCategoryService;
    }

    @GetMapping()
    public ResponseEntity<List<TicketCategory>> getCategories() {
        var categories = ticketCategoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @PostMapping()
    public ResponseEntity<TicketCategory> createCategory(
            @RequestBody TicketCategoryDto request
    ) {
        return ResponseEntity.ok(
                ticketCategoryService.createTicketCategory(request)
        );
    }

    @PatchMapping("/{categoryId}/description")
    public ResponseEntity<TicketCategory> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody String description,
            @RequestBody Boolean isActive
    ) {
        return ResponseEntity.ok(
                ticketCategoryService.updateCategory(
                        new TicketCategoryDto(categoryId, description, isActive)
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<TicketCategory> deleteCategory(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(
                ticketCategoryService.deleteCategory(categoryId)
        );
    }
}