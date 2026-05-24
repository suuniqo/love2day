package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.MessageDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto toDto(Message message);
    List<MessageDto> toDtoList(List<Message> messages);
}
