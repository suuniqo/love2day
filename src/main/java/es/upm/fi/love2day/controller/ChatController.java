package es.upm.fi.love2day.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    ChatService chatService;
    ChatMapper chatMapper;

    public ChatController(ChatService chatService, ChatMapper chatMapper) {
        this.chatService = chatService;
        this.chatMapper = chatMapper;
    }

    @GetMapping("/chats/{matchid}")
    public Page<ChatDto> obtenerMensajes(
        @PathVariable String matchid,
    ) {
        Pageable pageable = PageRequest.of(
            0,
            60,
            Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Mensaje> mensajes = mensajeService.getMensajes(matchid, pageable);
        return mensajes.map(ChatDto::fromEntity);
    }

    @PostMapping("/chats/{matchId}/msg")
    public Mensaje enviarMensaje(String content, String type) {
        return mensajeService.enviarMensaje(content, type);
    }
    
}
