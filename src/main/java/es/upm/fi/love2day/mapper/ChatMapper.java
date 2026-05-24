package es.upm.fi.love2day.mapper;

import es.upm.fi.love2day.dto.ChatDto;
import es.upm.fi.love2day.model.Chat;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatDto toDto(Chat chat);
}
