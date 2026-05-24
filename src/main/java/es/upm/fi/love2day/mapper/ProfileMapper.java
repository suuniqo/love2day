package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.ProfileDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);
    List<ProfileDto> toDtoList(List<Profile> profiles);
}
