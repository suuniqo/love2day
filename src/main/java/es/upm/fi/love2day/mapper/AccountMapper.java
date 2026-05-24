package es.upm.fi.love2day.mappers;
import es.upm.fi.love2day.dtos.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(Account account);
}
