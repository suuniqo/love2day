package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.VerificationDto;

@Mapper(componentModel = "spring")
    public interface VerificationMapper {
    VerificationDto toDto(Verification verification);
}
