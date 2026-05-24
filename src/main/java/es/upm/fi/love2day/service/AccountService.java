package es.upm.fi.love2day.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.upm.fi.love2day.events.VerificationResolvedEvent;
import es.upm.fi.love2day.exceptions.ConflictException;
import es.upm.fi.love2day.exceptions.NotFoundException;

import es.upm.fi.love2day.repository.AccountsRepository;

import es.upm.fi.love2day.model.Account;
import es.upm.fi.love2day.model.VerificationInquiry;
import es.upm.fi.love2day.model.VerificationStatus;

@Service
public class AccountService {
    private final AccountsRepository accountsRepository;

    private final VerificationService verificationService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(
        AccountsRepository repository,
        VerificationService verificationService
    ) {
        this.accountsRepository = repository;
        this.verificationService = verificationService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Account createAccount(String username, String email, String passwordRaw) {
        if (accountsRepository.existsByUsername(username)) {
            throw new ConflictException("Username already taken");
        }
        if (accountsRepository.existsByEmail(email)) {
            throw new ConflictException("Email already registered");
        }

        Account account = Account.create(
            username,
            email,
            passwordEncoder.encode(passwordRaw)
        );

        return accountsRepository.save(account);
    }

    @Transactional
    public VerificationInquiry startVerification(Long userId) {
        if (!accountsRepository.existsById(userId)) {
            throw new NotFoundException("Account not found: " + userId);
        }

        return verificationService.startVerification(userId);
    }

    @Transactional(readOnly = true)
    public VerificationStatus getVerificationStatus(Long userId) {
        if (!accountsRepository.existsById(userId)) {
            throw new NotFoundException("Account not found: " + userId);
        }

        return verificationService.getVerificationStatus(userId);
    }

    @Transactional
    public void verifyAccount(Long userId) {
        Account account = accountsRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("Account not found: " + userId));

        account.setVerified(true);
        accountsRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long userId) {
        Account account = accountsRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("Account not found: " + userId));

        accountsRepository.delete(account);

        // Se borran en cascada objetos dependientes
        verificationService.deleteById(userId);
        // TODO: faltan profile, swipes, match, chat, messages...
    }

    @EventListener
    public void onVerificationResolved(VerificationResolvedEvent event) {
        if (event.status().isVerified()) {
            verifyAccount(event.userId());
        }
    }
}
