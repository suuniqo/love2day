package es.upm.fi.love2day.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.repository.ChatsRepository;
import es.upm.fi.love2day.model.Message;

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

    public List<Message> getMessages(Long matchId) {
        Optional<Chat> chatOpt = chatsRepository.findByMatchId(matchId);
        if (chatOpt.isEmpty()) {
            throw new RuntimeException("Chat not found for matchId: " + matchId);
        }
        return chatOpt.get().getMessages();
    }

    public Message sendMessage(Long matchId, Long senderId, String content) {
        Optional<Chat> chatOpt = chatsRepository.findByMatchId(matchId);
        if (chatOpt.isEmpty()) {
            throw new RuntimeException("Chat not found for matchId: " + matchId);
        }
        Chat chat = chatOpt.get();
        Message message = Message.create(senderId, content);
        chat.addMessage(message);
        chatsRepository.save(chat);
        return message;
    }
}
