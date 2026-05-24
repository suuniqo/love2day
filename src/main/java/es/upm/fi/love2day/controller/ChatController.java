package es.upm.fi.love2day.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import es.upm.fi.love2day.dto.ChatDto;
import es.upm.fi.love2day.mapper.ChatMapper;
import es.upm.fi.love2day.model.Message;
import es.upm.fi.love2day.service.ChatService;

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
        @PathVariable String matchId
    ) {
        Pageable pageable = PageRequest.of(
            0,
            60,
            Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Message> mensajes = chatService.getMensajes(matchId, pageable);
        return mensajes.map(ChatDto::fromEntity);
    }

    @PostMapping("/chats/{matchId}/msg")
    public Message enviarMensaje(String content, String type) {
        return message.enviarMensaje(content, type);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatDto createChat(@RequestBody CreateChatDto request) {
        Chat chat = chatService.createChat(
            request.matchid()
        );

        return chatMapper.toDto(chat);
    }

    @DeleteMapping("/{matchId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChat(@PathVariable Long matchId) {
        chatService.deleteChat(matchId);
    }

    //TODO:Qué pongo aquí?
    @GetMapping("/{matchId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long matchId) {
        return messageMapper.toDtoList(chatService.getMessages(matchId));
    }
}
