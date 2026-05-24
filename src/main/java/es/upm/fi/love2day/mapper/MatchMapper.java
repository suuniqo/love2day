package es.upm.fi.love2day.mapper;

import org.mapstruct.Mapper;

import es.upm.fi.love2day.dto.MatchDto;
import es.upm.fi.love2day.model.Match;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    MatchDto toDto(Match match);
}
