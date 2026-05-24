package es.upm.fi.love2day.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.events.VerificationResolvedEvent;
import es.upm.fi.love2day.exceptions.ConflictException;
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
        verificationsRepository.findById(userId).ifPresent(v -> {
            if (v.getStatus() == VerificationStatus.VERIFIED) {
                throw new ConflictException("Account is already verified");
            }
            if (v.getStatus() == VerificationStatus.PENDING) {
                throw new ConflictException("Verification already in progress");
            }
        });

        Verification verification = Verification.create(
            userId,
            verifier.createInquiry(userId)
        );

        verificationsRepository.save(verification);

        return verification.getInquiry();
    }

    @Transactional(readOnly = true)
    public VerificationStatus getVerificationStatus(Long userId) {
        return verificationsRepository
            .findById(userId)
            .map(Verification::getStatus)
            .orElse(VerificationStatus.UNVERIFIED);
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
    public void deleteById(Long userId) {
        verificationsRepository.deleteById(userId);
    }
}
