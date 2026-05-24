package es.upm.fi.love2day.service;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.model.MessageStatus;
import es.upm.fi.love2day.repository.MessagesRepository;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final ChatService chatService;

    public MessageService(MessagesRepository repository, ChatService chat) {
        this.messagesRepository = repository;
        this.chatService = chat;
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

    public Page<Message> getMesagges(Long matchId, Pageable pageable) {
        return messagesRepository.findByMatchId(matchId, pageable);
    }

    public Message sendMessage(Long senderId, Long receptorId, Long matchId, String mediaKind, String content) {
        Message message = Message.create(senderId, matchId, mediaKind, content);
        chatService.updateLastMessage(matchId, message);

        message = messagesRepository.save(message);
        notifyNewMessage(receptorId, matchId);

        message.setStatus(MessageStatus.DELIVERED);
        return messagesRepository.save(message);
    }
}
