package es.upm.fi.love2day.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.upm.fi.love2day.exceptions.BadRequestException;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.model.MessageStatus;
import es.upm.fi.love2day.repository.MessagesRepository;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final MessageWebSocketHandler socketHandler;

    public MessageService(
        MessagesRepository repository,
        MessageWebSocketHandler socketHandler
    ) {
        this.messagesRepository = repository;
        this.socketHandler = socketHandler;
    }

    @Transactional
    public Page<Message> getMesagges(Long matchId, Long receiverId, Pageable pageable) {
        Page<Message> messages = messagesRepository.findByMatchId(matchId, pageable);

        List<Message> unread = messages
            .stream()
            .filter(msg -> !msg.isReadBy(receiverId))
            .toList();

        unread.forEach(msg -> msg.markAsRead());
        messagesRepository.saveAll(unread);

        return messages;
    }

    @Transactional
    public void deleteMessage(Long id, Long userId) {
        Message message = messagesRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Message not found: " + id));

        if (message.getSenderId() != userId) {
            throw new BadRequestException(
                "Message can only be deleted by senderId: " + userId
            );
        }

        messagesRepository.deleteById(id);
    }

    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, Long matchId, String mediaKind, String content) {
        Message message = Message.create(senderId, matchId, mediaKind, content);
        messagesRepository.save(message);

        socketHandler.notifyNewMessage(receiverId, matchId);

        message.setStatus(MessageStatus.DELIVERED);
        return messagesRepository.save(message);
    }

    @Transactional
    public void deleteByMatchIds(List<Long> matchIds) {
        messagesRepository.deleteAllByMatchIdIn(matchIds);
    }
}
