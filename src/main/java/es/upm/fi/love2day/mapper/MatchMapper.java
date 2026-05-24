package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.MatchDto;

import java.util.List;

//TODO: Ver si implementar nuestros mappers o utilizamos las funciones de service y con estos los convertimos
@Mapper(componentModel = "spring")
public interface MatchMapper {
    MatchDto toDto(Match match);
    List<MatchDto> toDtoList(List<Match> matches);
}
