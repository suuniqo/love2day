package es.upm.fi.love2day.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.model.MessageStatus;
import es.upm.fi.love2day.repository.MessagesRepository;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;

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
            .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setStatus(status); 
    }

    public void deleteMessage(Long id) {
        messagesRepository.deleteById(id);
    }
}
