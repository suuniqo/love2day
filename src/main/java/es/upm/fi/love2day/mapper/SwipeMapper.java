package es.upm.fi.love2day.mapper;
import es.upm.fi.love2day.dto.SwipeDto;
import es.upm.fi.love2day.model.Swipe;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SwipeMapper {
    SwipeDto toDto(Swipe swipe);
}
