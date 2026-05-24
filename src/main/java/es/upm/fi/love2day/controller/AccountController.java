package es.upm.fi.love2day.controller;

import es.upm.fi.love2day.dto.AccountDto;
import es.upm.fi.love2day.dto.CreateAccountRequest;
import es.upm.fi.love2day.mapper.AccountMapper;
import es.upm.fi.love2day.model.Account;
import es.upm.fi.love2day.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);

        return accountMapper.toDto(account);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long userId) {
        accountService.deleteAccount(userId);
    }
}
