package es.upm.fi.love2day.mapper;
import es.upm.fi.love2day.dto.ProfileDto;
import es.upm.fi.love2day.model.Profile;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);
}
