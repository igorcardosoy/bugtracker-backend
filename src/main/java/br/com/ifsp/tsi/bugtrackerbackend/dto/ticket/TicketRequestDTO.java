package br.com.ifsp.tsi.bugtrackerbackend.dto.ticket;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record TicketRequestDTO(
        long receiverId,
        long ticketCategoryId,
        long ratingId,
        String description,
        String ticketStatus,
        List<String> imagesAttachedPaths
) { }