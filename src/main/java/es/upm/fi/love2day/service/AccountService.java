package es.upm.fi.love2day.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.upm.fi.love2day.model.Account;
import es.upm.fi.love2day.repository.AccountsRepository;

@Service
public class AccountService {
    private final AccountsRepository accountsRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(AccountsRepository repository) {
        this.accountsRepository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Account createAccount(String username, String email, String passwordRaw) {
        if (accountsRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already taken");
        }
        if (accountsRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        String passwordHash = passwordEncoder.encode(passwordRaw);
        Account account = Account.create(username, email, passwordHash);

        return accountsRepository.save(account);
    }

    public Optional<Account> findById(Long id) {
        return accountsRepository.findById(id);
    }

    public Optional<Account> findByUsername(String username) {
        return accountsRepository.findByUsername(username);
    }

    // public Account verifyAccount(Long id, Document document) {
    //     Account account = accountsRepository
    //         .findById(id)
    //         .orElseThrow(() -> new RuntimeException("Account not found"));
    //
    //     account.setVerified(true); // TODO: implementar verificación de verdad
    //
    //     return accountsRepository.save(account);
    // }
    //

    public void deleteAccount(Long id) {
        accountsRepository.deleteById(id);
    }
}
