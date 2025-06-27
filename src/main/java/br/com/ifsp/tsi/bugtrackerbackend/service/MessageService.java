package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.message.MessageRequestDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.message.MessageResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.exception.MessageNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.exception.TicketNotFoundException;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final UserService userService;
    private final TicketService ticketService;

    private final MessageRepository messageRepository;

    public MessageService(
            UserService userService,
            TicketService ticketService,
            MessageRepository messageRepository
    ) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.messageRepository = messageRepository;
    }

    public List<MessageResponseDTO> getAllMessagesByTicket(long ticketId) {
        var savedMessages = messageRepository.findAll()
                .stream()
                .filter(message -> message.getTicket().getTicketId() == ticketId)
                .toList();

        return BuildMessageResponseDTOs(savedMessages);
    }

    private List<MessageResponseDTO> BuildMessageResponseDTOs(List<Message> messages) {
        List<MessageResponseDTO> messageResponseDTOs = new ArrayList<>();

        for (var message : messages)
            messageResponseDTOs.add(MessageResponseDTO.fromMessage(message));

        return messageResponseDTOs;
    }

    public MessageResponseDTO createTicketMessage(long ticketId, MessageRequestDTO request) {
        var signedInUserDto = userService.getUserSignedIn();
        var savedTicket = ticketService.getTicketById(ticketId);

        if (savedTicket == null)
            throw new TicketNotFoundException();

        var user = new User(signedInUserDto);
        var message = new Message(request, user, savedTicket);

        var savedMessage = messageRepository.save(message);

        return MessageResponseDTO.fromMessage(savedMessage);
    }

    public void updateMessage(long messageId, String message) {
        var optional = messageRepository.findById(messageId);

        if (optional.isEmpty())
            throw new MessageNotFoundException();

        var savedMessage = optional.get();

        savedMessage.setMessage(message);
        savedMessage.setWasEdited(true);

        messageRepository.save(savedMessage);
    }
}