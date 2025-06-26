package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.MessageDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.repository.MessageRepository;
import org.springframework.stereotype.Service;

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

    public Message updateMessage(long messageId, String message) {
        var optional = messageRepository.findById(messageId);

        if (optional.isEmpty())
            return new Message();

        var messageMessage = optional.get();

        messageMessage.setMessage(message);
        messageMessage.setWasEdited(true);

        return messageRepository.save(messageMessage);
    }

    public List<Message> getAllMessagesByTicket(long ticketId) {
        return messageRepository.findAll().stream().filter(
                message -> message.getTicket().getTicketId() == ticketId)
                .toList();
    }

    public Message createTicketMessage(long ticketId, MessageDto request) {
        var userDto = userService.getUserSignedIn();
        var ticket = ticketService.getTicketById(ticketId);


        var user = new User(userDto);
        var message = new Message(request, user, ticket);

        return messageRepository.save(message);
    }
}