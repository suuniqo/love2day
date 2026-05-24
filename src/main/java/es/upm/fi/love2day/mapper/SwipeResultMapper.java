package es.upm.fi.love2day.mapper;

import es.upm.fi.love2day.dto.SwipeResultDto;
import es.upm.fi.love2day.service.SwipeService.SwipeResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SwipeMapper.class, MatchMapper.class})
public interface SwipeResultMapper {
    SwipeResultDto toDto(SwipeResult result);
}
