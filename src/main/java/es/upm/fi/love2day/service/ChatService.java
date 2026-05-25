package es.upm.fi.love2day.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.exceptions.BadRequestException;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.repository.ChatsRepository;
import es.upm.fi.love2day.model.Message;

@Service
public class ChatService {
    private final ChatsRepository chatsRepository;
    private final MessageService messageService;
    private final MatchService matchService;

    public ChatService(
        ChatsRepository repository,
        MessageService messageService,
        @Lazy MatchService matchService
    ) {
        this.chatsRepository = repository;
        this.messageService = messageService;
        this.matchService = matchService;
    }

    @Transactional
    public Chat createChat(Long matchId) {
        if (!matchService.existsMatch(matchId)) {
            throw new NotFoundException("Match not found: " + matchId);
        }

        Chat chat = Chat.create(matchId);

        return chatsRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public Optional<Chat> getChat(Long id) {
        return chatsRepository.findById(id);
    }

    @Transactional
    public Page<Message> getMessages(Long matchId, Long receiverId, Pageable pageable) {
        if (!chatsRepository.existsById(matchId)) {
            throw new NotFoundException("Chat not found: " + matchId);
        }
        if (matchService.findOpposite(receiverId, matchId).isEmpty()) {
            throw new BadRequestException("Match doesn't contain receiverId: " + receiverId);
        }

        return messageService.getMesagges(matchId, receiverId, pageable);
    }

    @Transactional
    public Message sendMessage(Long matchId, Long senderId, String mediaKind, String content) {
        if (!chatsRepository.existsById(matchId)) {
            throw new NotFoundException("Chat not found: " + matchId);
        }

        Long receiverId = matchService
            .findOpposite(senderId, matchId)
            .orElseThrow(() -> new BadRequestException("Match doesn't contain senderId: " + senderId));

        return messageService.sendMessage(senderId, receiverId, matchId, mediaKind, content);
    }

    @Transactional
    public void blockChat(Long matchId, Long userId) {
        if (matchService.findOpposite(userId, matchId).isEmpty()) {
            throw new BadRequestException("Match doesn't contain userId: " + userId);
        }

        Chat chat = chatsRepository
            .findById(matchId)
            .orElseThrow(() -> new NotFoundException("Chat not found: " + matchId));

        chat.block();
        chatsRepository.save(chat);
    }

    @Transactional
    public void deleteMessage(Long msgId, Long userId) {
        messageService.deleteMessage(msgId, userId);
    }

    @Transactional
    public void deleteByMatchIds(List<Long> matchIds) {
        chatsRepository.deleteAllByIdInBatch(matchIds);
    }
}
