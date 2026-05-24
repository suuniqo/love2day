package es.upm.fi.love2day.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.repository.ChatsRepository;
import es.upm.fi.love2day.model.Message;

@Service
public class ChatService {
    private final ChatsRepository chatsRepository;
    private final MessageService messageService;

    public ChatService(
        ChatsRepository repository,
        MessageService messageService
    ) {
        this.chatsRepository = repository;
        this.messageService = messageService;
    }

    @Transactional
    public Chat createChat(Long matchId) {
        Chat chat = Chat.create(matchId);

        return chatsRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public Optional<Chat> getChat(Long id) {
        return chatsRepository.findById(id);
    }

    @Transactional
    public Page<Message> getMessages(Long matchId, Pageable pageable) {
        if (!chatsRepository.existsById(matchId)) {
            throw new NotFoundException("Chat not found: " + matchId);
        }

        return messageService.getMesagges(matchId, pageable);
    }

    @Transactional
    public Message sendMessage(Long matchId, Long senderId, String mediaKind, String content) {
        if (!chatsRepository.existsById(matchId)) {
            throw new NotFoundException("Chat not found: " + matchId);
        }

        return messageService.sendMessage(senderId, matchId, mediaKind, content);
    }

    @Transactional
    public void blockChat(Long matchId) {
        Chat chat = chatsRepository
            .findById(matchId)
            .orElseThrow(() -> new NotFoundException("Chat not found: " + matchId));

        chat.block();
        chatsRepository.save(chat);
    }

    @Transactional
    public void deleteMessage(Long msgId) {
        messageService.deleteMessage(msgId);
    }

    @Transactional
    public void deleteByMatchIds(List<Long> matchIds) {
        chatsRepository.deleteAllByIdInBatch(matchIds);
    }
}
