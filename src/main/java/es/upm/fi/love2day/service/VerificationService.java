package es.upm.fi.love2day.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.events.VerificationResolvedEvent;
import es.upm.fi.love2day.exceptions.ConflictException;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.interfaces.Verifier;
import es.upm.fi.love2day.model.Verification;
import es.upm.fi.love2day.model.VerificationInquiry;
import es.upm.fi.love2day.model.VerificationStatus;
import es.upm.fi.love2day.repository.VerificationsRepository;

@Service
public class VerificationService {
    private final VerificationsRepository verificationsRepository;

    private final VerificationWebSocketHandler socketHandler;
    private final ApplicationEventPublisher eventPublisher;

    private final Verifier verifier;

    public VerificationService(
        VerificationsRepository verificationsRepository,
        VerificationWebSocketHandler socketHandler,
        ApplicationEventPublisher eventPublisher,
        Verifier verifier
    ) {
        this.verificationsRepository = verificationsRepository;
        this.socketHandler = socketHandler;
        this.eventPublisher = eventPublisher;
        this.verifier = verifier;
    }

    @Transactional
    public VerificationInquiry startVerification(Long userId) {
        Verification verification = verificationsRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        if (verification.getStatus() == VerificationStatus.VERIFIED) {
            throw new ConflictException("Account is already verified");
        }
        if (verification.getStatus() == VerificationStatus.PENDING) {
            throw new ConflictException("Verification already in progress");
        }

        VerificationInquiry inquiry = verifier.createInquiry(userId);

        verification.restart();
        verificationsRepository.save(verification);

        return inquiry;
    }

    @Transactional(readOnly = true)
    public VerificationStatus getVerificationStatus(Long userId) {
        return verificationsRepository
            .findById(userId)
            .map(Verification::getStatus)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    @Transactional
    public void createVerification(Long userId) {
        if (verificationsRepository.existsById(userId)) {
            throw new IllegalStateException("Verification already exists for user: " + userId);
        }

        Verification verification = Verification.create(userId);
        verificationsRepository.save(verification);
    }


    @Transactional
    public void resolveVerification(Long userId, VerificationStatus result) {
        Verification v = verificationsRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("No verification for user: " + userId));

        v.setStatus(result);
        verificationsRepository.save(v);

        eventPublisher.publishEvent(new VerificationResolvedEvent(userId, result));

        socketHandler.notify(userId, result);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        verificationsRepository.deleteById(userId);
    }
}
