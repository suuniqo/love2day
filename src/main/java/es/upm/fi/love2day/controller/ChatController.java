package es.upm.fi.love2day.controller;

import es.upm.fi.love2day.dto.ChatDto;
import es.upm.fi.love2day.dto.CreateChatDto;
import es.upm.fi.love2day.mapper.ChatMapper;
import es.upm.fi.love2day.mapper.MessageMapper;
import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    public ChatController(ChatService chatService, ChatMapper chatMapper, MessageMapper messageMapper) {
        this.chatService = chatService;
        this.chatMapper = chatMapper;
        this.messageMapper = messageMapper;
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

    //TODO:Qué pongo aquí?
    @PostMapping("/{matchId}/messages")
    public MessageDto sendMessage(@PathVariable Long matchId, @RequestBody SendMessageDto request) {
        Message message = chatService.sendMessage(matchId, request.senderId(), request.content());
        return messageMapper.toDto(message);
    }
}

