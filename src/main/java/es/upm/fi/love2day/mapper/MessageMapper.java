package es.upm.fi.love2day.mapper;

import es.upm.fi.love2day.dto.MessageDto;
import es.upm.fi.love2day.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto toDto(Message message);
}
