package es.upm.fi.love2day.integration;

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
        Verification v = Verification.create(
            USER_ID,
            new VerificationInquiry("inquiry-id", "session-token")
        );
        v.setStatus(status);
        return v;
    }

    // C1: no existe verificación previa → éxito
    @Test
    void startVerification_noExistingVerification_returnsInquiry() {
        VerificationInquiry inquiry = new VerificationInquiry("id-123", "token-abc");

        when(verificationsRepository.findById(USER_ID)).thenReturn(Optional.empty());
        when(verifier.createInquiry(USER_ID)).thenReturn(inquiry);

        VerificationInquiry result = verificationService.startVerification(USER_ID);

        assertNotNull(result);
        assertEquals("id-123", result.getId());
        verify(verificationsRepository).save(any(Verification.class));
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

    // C4: estado REJECTED → puede reintentar, éxito
    @Test
    void startVerification_previouslyRejected_returnsNewInquiry() {
        VerificationInquiry inquiry = new VerificationInquiry("id-456", "token-xyz");

        when(verificationsRepository.findById(USER_ID))
            .thenReturn(Optional.of(verificationWithStatus(VerificationStatus.REJECTED)));
        when(verifier.createInquiry(USER_ID)).thenReturn(inquiry);

        VerificationInquiry result = verificationService.startVerification(USER_ID);

        assertNotNull(result);
        assertEquals("id-456", result.getId());
        verify(verificationsRepository).save(any(Verification.class));
    }
}
