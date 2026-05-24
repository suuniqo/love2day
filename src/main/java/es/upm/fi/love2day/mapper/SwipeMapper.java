package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.SwipeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SwipeMapper {
    SwipeDto toDto(Swipe swipe);
    List<SwipeDto> toDtoList(List<Swipe> swipes);
//TODO: Tenemos que hacer algo con preferences
}
