package es.upm.fi.love2day.service;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.upm.fi.love2day.exceptions.BadRequestException;
import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.model.MessageStatus;
import es.upm.fi.love2day.repository.MessagesRepository;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final MatchService matchService;
    private final MessageWebSocketHandler socketHandler;

    public MessageService(
        MessagesRepository repository,
        MatchService matchService,
        MessageWebSocketHandler socketHandler
    ) {
        this.messagesRepository = repository;
        this.matchService = matchService;
        this.socketHandler = socketHandler;
    }

    public Page<Message> getMesagges(Long matchId, Pageable pageable) {
        Page<Message> messages = messagesRepository.findByMatchId(matchId, pageable);

        List<Message> unread = messages
            .stream()
            .filter(msg -> !msg.isRead())
            .toList();

        unread.forEach(msg -> msg.markAsRead());
        messagesRepository.saveAll(unread);

        return messages;
    }

    public void deleteMessage(Long id) {
        messagesRepository.deleteById(id);
    }

    public Message sendMessage(Long senderId, Long matchId, String mediaKind, String content) {
        Message message = Message.create(senderId, matchId, mediaKind, content);
        messagesRepository.save(message);

        Long receiverId = matchService
            .findOpposite(senderId, matchId)
            .orElseThrow(() -> new BadRequestException("Match doesn't contain senderId: " + senderId));

        socketHandler.notifyNewMessage(receiverId, matchId);

        message.setStatus(MessageStatus.DELIVERED);
        return messagesRepository.save(message);
    }
}
