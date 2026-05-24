package es.upm.fi.love2day.service;
import org.springframework.stereotype.Service;
import java.util.Optional;
import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.repository.MessagesRepository;

@Service
public class MessageService {
    private final MessagesRepository messagesRepository;

    public MessageService(MessagesRepository repository) {
        this.messagesRepository = repository;
    }

    public Message createMessage(Long id, Long senderId, String content, MediaType type) {
        Message mensaje = new Message(id, senderId, content, type);

        return messagesRepository.save(mensaje);
    }

    public Optional<Message> findById(Long id) {
        return messagesRepository.findById(id);
    }

    public Optional<Message> findByChatId(Long chatId) {
        return messagesRepository.findByChatId(chatId);
    }

    public void setEstado(Long id, MessageStatus status) {
        Message mensaje = messagesRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        mensaje.setStatus(status); 
    }

    public void deleteMessage(Long id) {
        messagesRepository.deleteById(id);
    }
}
