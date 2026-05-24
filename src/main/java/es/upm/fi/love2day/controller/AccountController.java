package es.upm.fi.love2day.controller;

import es.upm.fi.love2day.dto.AccountDto;
import es.upm.fi.love2day.dto.CreateAccountDto;
import es.upm.fi.love2day.mapper.AccountMapper;
import es.upm.fi.love2day.model.Account;
import es.upm.fi.love2day.model.VerificationInquiry;
import es.upm.fi.love2day.model.VerificationStatus;
import es.upm.fi.love2day.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody CreateAccountDto request) {
        Account account = accountService.createAccount(
            request.username(),
            request.email(),
            request.password()
        );

        return accountMapper.toDto(account);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long userId) {
        accountService.deleteAccount(userId);
    }

    @PostMapping("/{userId}/verification")
    @ResponseStatus(HttpStatus.CREATED)
    public VerificationInquiry startVerification(@PathVariable Long userId) {
        return accountService.startVerification(userId);
    }

    @GetMapping("/{userId}/verification")
    public VerificationStatus getVerificationStatus(@PathVariable Long userId) {
        return accountService.getVerificationStatus(userId);
    }
}
