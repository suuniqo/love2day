package es.upm.fi.love2day.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.repository.ChatsRepository;

@Service
public class ChatService {
    private final ChatsRepository chatsRepository;

    public ChatService(ChatsRepository repository) {
        this.chatsRepository = repository;
    }

    public Chat createChat(Long matchId) {
        Chat chat = Chat.create(matchId);

        return chatsRepository.save(chat);
    }

    public Optional<Chat> findById(Long id) {
        return chatsRepository.findById(id);
    }

    public void deleteChat(Long id) {
        chatsRepository.deleteById(id);
    }
}
