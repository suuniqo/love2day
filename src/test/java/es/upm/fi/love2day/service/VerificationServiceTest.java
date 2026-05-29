package es.upm.fi.love2day.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import es.upm.fi.love2day.exceptions.ConflictException;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.interfaces.Verifier;
import es.upm.fi.love2day.model.Verification;
import es.upm.fi.love2day.model.VerificationInquiry;
import es.upm.fi.love2day.model.VerificationStatus;
import es.upm.fi.love2day.repository.VerificationsRepository;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private VerificationsRepository verificationsRepository;

    @Mock
    private VerificationWebSocketHandler socketHandler;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private Verifier verifier;

    @InjectMocks
    private VerificationService verificationService;

    private final Long USER_ID = 1L;

    private Verification verificationWithStatus(VerificationStatus status) {
        Verification v = Verification.create(USER_ID);
        v.setStatus(status);
        return v;
    }

    // C1: no existe verificación → NotFoundException
    @Test
    void startVerification_noVerificationExists_throwsNotFound() {
        when(verificationsRepository.findById(USER_ID))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> verificationService.startVerification(USER_ID));

        verify(verificationsRepository, never()).save(any());
    }

    // C2: estado VERIFIED → ConflictException
    @Test
    void startVerification_alreadyVerified_throwsConflict() {
        when(verificationsRepository.findById(USER_ID))
            .thenReturn(Optional.of(verificationWithStatus(VerificationStatus.VERIFIED)));

        assertThrows(ConflictException.class,
            () -> verificationService.startVerification(USER_ID));

        verify(verificationsRepository, never()).save(any());
    }

    // C3: estado PENDING → ConflictException
    @Test
    void startVerification_verificationPending_throwsConflict() {
        when(verificationsRepository.findById(USER_ID))
            .thenReturn(Optional.of(verificationWithStatus(VerificationStatus.PENDING)));

        assertThrows(ConflictException.class,
            () -> verificationService.startVerification(USER_ID));

        verify(verificationsRepository, never()).save(any());
    }

    // C4a: estado UNVERIFIED → éxito
    @Test
    void startVerification_unverified_returnsInquiry() {
        VerificationInquiry inquiry = new VerificationInquiry("id-123", "token-abc");
        Verification verification = verificationWithStatus(VerificationStatus.UNVERIFIED);

        when(verificationsRepository.findById(USER_ID))
            .thenReturn(Optional.of(verification));
        when(verifier.createInquiry(USER_ID)).thenReturn(inquiry);

        VerificationInquiry result = verificationService.startVerification(USER_ID);

        assertNotNull(result);
        assertEquals("id-123", result.getId());
        verify(verificationsRepository).save(verification);
    }

    // C4b: estado REJECTED → puede reintentar, éxito
    @Test
    void startVerification_previouslyRejected_returnsInquiry() {
        VerificationInquiry inquiry = new VerificationInquiry("id-456", "token-xyz");
        Verification verification = verificationWithStatus(VerificationStatus.REJECTED);

        when(verificationsRepository.findById(USER_ID))
            .thenReturn(Optional.of(verification));
        when(verifier.createInquiry(USER_ID)).thenReturn(inquiry);

        VerificationInquiry result = verificationService.startVerification(USER_ID);

        assertNotNull(result);
        assertEquals("id-456", result.getId());
        verify(verificationsRepository).save(verification);
    }
}
