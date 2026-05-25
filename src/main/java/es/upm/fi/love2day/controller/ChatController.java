package es.upm.fi.love2day.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.upm.fi.love2day.dto.ChatDto;
import es.upm.fi.love2day.dto.MessageDto;
import es.upm.fi.love2day.dto.SendMessageRequest;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.mapper.ChatMapper;
import es.upm.fi.love2day.mapper.MessageMapper;
import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    public ChatController(
        ChatService chatService,
        ChatMapper chatMapper,
        MessageMapper messageMapper
    ) {
        this.chatService = chatService;
        this.chatMapper = chatMapper;
        this.messageMapper = messageMapper;
    }

    @PostMapping("/{matchId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatDto createChat(@PathVariable Long matchId) {
        return chatMapper.toDto(chatService.createChat(matchId));
    }

    @GetMapping("/{matchId}")
    public ChatDto getChat(@PathVariable Long matchId) {
        Chat chat = chatService
            .getChat(matchId)
            .orElseThrow(() -> new NotFoundException("Chat not found: " + matchId));

        return chatMapper.toDto(chat);
    }

    @GetMapping("/{matchId}/msg/{userId}")
    public Page<MessageDto> getMessages(
        @PathVariable Long matchId,
        @PathVariable Long userId,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return chatService.getMessages(matchId, userId, pageable).map(messageMapper::toDto);
    }

    @PostMapping("/{matchId}/msg/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto sendMessage(
        @PathVariable Long matchId,
        @PathVariable Long userId,
        @RequestBody SendMessageRequest request
    ) {
        return messageMapper.toDto(
            chatService.sendMessage(matchId, userId, request.mediaKind(), request.content())
        );
    }

    @PatchMapping("/{matchId}/block/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockChat(@PathVariable Long matchId, @PathVariable Long userId) {
        chatService.blockChat(matchId, userId);
    }
    
    @DeleteMapping("/{matchId}/msg/{userId}/delete/{msgId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(
        @PathVariable Long matchId,
        @PathVariable Long userId,
        @PathVariable Long msgId
    ) {
        chatService.deleteMessage(msgId, userId);
    }
}
