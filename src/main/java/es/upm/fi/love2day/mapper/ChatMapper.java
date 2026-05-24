package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.ChatDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatDto toDto(Chat chat);
    List<ChatDto> toDtoList(List<Chat> chats);
}
