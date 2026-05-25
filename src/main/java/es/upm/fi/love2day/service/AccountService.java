package es.upm.fi.love2day.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.upm.fi.love2day.dto.CreateAccountRequest;
import es.upm.fi.love2day.events.VerificationResolvedEvent;
import es.upm.fi.love2day.exceptions.ConflictException;
import es.upm.fi.love2day.exceptions.NotFoundException;

import es.upm.fi.love2day.repository.AccountsRepository;

import es.upm.fi.love2day.model.Account;

@Service
public class AccountService {
    private final AccountsRepository accountsRepository;

    private final VerificationService verificationService;
    private final SwipeService swipeService;
    private final ProfileService profileService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(
        AccountsRepository repository,
        VerificationService verificationService,
        SwipeService swipeService,
        ProfileService profileService
    ) {
        this.accountsRepository = repository;

        this.verificationService = verificationService;
        this.swipeService = swipeService;
        this.profileService = profileService;

        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        if (accountsRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username already taken");
        }
        if (accountsRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already registered");
        }

        Account account = Account.create(
            request.username(),
            request.email(),
            passwordEncoder.encode(request.password())
        );

        accountsRepository.save(account);

        profileService.createProfile(account.getId());

        return account;
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

        // Se borran en cascada objetos dependientes
        verificationService.deleteByUserId(userId);
        profileService.deleteByUserId(userId);
        swipeService.deleteByUserId(userId);

        accountsRepository.delete(account);
    }

    @EventListener
    public void onVerificationResolved(VerificationResolvedEvent event) {
        if (event.status().isVerified()) {
            verifyAccount(event.userId());
        }
    }
}
