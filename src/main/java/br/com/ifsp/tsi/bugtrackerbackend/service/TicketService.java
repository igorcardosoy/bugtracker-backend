package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketCategoryNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.model.enums.TicketStatus;
import br.com.ifsp.tsi.bugtrackerbackend.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
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

    public static String saveTicketImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path uploadDir = Paths.get("uploads/ticket-images");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            log.info("Imagem do ticket salva: {}", fileName);
            return fileName;

        } catch (IOException e) {
            log.error("Erro ao salvar imagem do ticket", e);
            throw new RuntimeException("Não foi possível salvar a imagem do ticket: " + e.getMessage(), e);
        }
    }

    public TicketResponseDTO createTicket(TicketRequestDTO request, MultipartFile[] images) {
        var userDto = userService.getUserSignedIn();
        var sender = new User(userDto);

        var receiver = userService.getUserById(request.receiverId());

        var category = ticketCategoryService.getCategoryById(request.ticketCategoryId());

        if (category == null)
            throw new TicketCategoryNotFoundException();

        var timestamp = LocalDateTime.now();

        var ticket = new Ticket(
                request,
                sender,
                receiver,
                timestamp,
                category);

        if (images != null) {
            for (MultipartFile image : images) {
                String path = saveTicketImage(image);
                ticket.getImagesAttachedPaths().add(path);
            }
        }

        var savedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromTicket(savedTicket);
    }

    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO request, MultipartFile[] images) {
        var ticket = getTicketById(id);

        if (ticket == null) {
            throw new TicketNotFoundException();
        }

        ticket.setDescription(request.description());
        ticket.setTicketStatus(TicketStatus.valueOf(request.ticketStatus()));

        var receiver = userService.getUserById(request.receiverId());
        ticket.setReceiver(receiver);

        var category = ticketCategoryService.getCategoryById(request.ticketCategoryId());
        if (category == null)
            throw new TicketCategoryNotFoundException();
        ticket.setTicketCategory(category);

        if (images != null && images.length > 0) {
            List<String> paths = new ArrayList<>();
            for (MultipartFile image : images) {
                String path = saveTicketImage(image);
                paths.add(path);
            }
            ticket.setImagesAttachedPaths(paths);
        }

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