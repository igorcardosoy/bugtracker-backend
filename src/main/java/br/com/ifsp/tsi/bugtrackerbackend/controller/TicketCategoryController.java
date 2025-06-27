package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.TicketCategoryDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.TicketCategory;
import br.com.ifsp.tsi.bugtrackerbackend.service.TicketCategoryService;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ticketCategoryService.createTicketCategory(request)
        );
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<TicketCategory> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody TicketCategoryDto request
    ) {
        return ResponseEntity.ok(
                ticketCategoryService.updateCategory(
                        new TicketCategoryDto(categoryId, request.description(), request.isActive())
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long categoryId
    ) {
        ticketCategoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}