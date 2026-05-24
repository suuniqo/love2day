package es.upm.fi.love2day.mapper;

import es.upm.fi.love2day.dto.AccountDto;
import es.upm.fi.love2day.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(Account account);
}
