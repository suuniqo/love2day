package es.upm.fi.love2day.service;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.model.MessageStatus;
import es.upm.fi.love2day.repository.MessagesRepository;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final MatchService matchService;

    public MessageService(MessagesRepository repository) {
        this.messagesRepository = repository;
    }

    public Message createMessage(Long senderId, Long matchId, String mediaKind, String content) {
        Message message = Message.create(senderId, matchId, mediaKind, content);

        return messagesRepository.save(message);
    }

    public Optional<Message> findById(Long id) {
        return messagesRepository.findById(id);
    }

    public void setEstado(Long id, MessageStatus status) {
        Message message = messagesRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Message not found: " + id));

        message.setStatus(status); 
    }

    public void deleteMessage(Long id) {
        messagesRepository.deleteById(id);
    }

    public Page<Message> getMesagges(Long matchId, Pageable pageable) {
        return messagesRepository.findByMatchId(matchId, pageable);
    }

    public Message sendMessage(Long senderId, Long matchId, String mediaKind, String content) {
        Message message = Message.create(senderId, matchId, mediaKind, content);

        messagesRepository.save(message);

        notifyNewMessage(receptorId, matchId);

        message.setStatus(MessageStatus.DELIVERED);

        return messagesRepository.save(message);
    }
}
