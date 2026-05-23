package es.upm.fi.love2day.service;

import java.util.Optional;

import es.upm.fi.love2day.model.Document;
import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.repository.ChatsRepository;

@Service
public class ChatService {
    private final ChatsRepository chatsRepository;

    public ChatService(ChatsRepository repository) {
        this.chatsRepository = repository;
    }

    public Chat createChat(Long chatId, Long matchId, Acount user) {
        Chat Chat = new Chat(chatId, matchId, user);

        return chatsRepository.save(Chat);
    }

    public Optional<Chat> findById(Long id) {
        return chatsRepository.findById(id);
    }

    public void deleteChat(Long id) {
        chatsRepository.deleteById(id);
    }

    //TODO:¿No tiene más?
}
