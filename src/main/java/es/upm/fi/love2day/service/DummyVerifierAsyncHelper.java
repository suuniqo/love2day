package es.upm.fi.love2day.service;

import es.upm.fi.love2day.model.VerificationStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DummyVerifierAsyncHelper {

    private final VerificationService verificationService;

    public DummyVerifierAsyncHelper(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @Async
    public void scheduleResolution(Long userId) {
        try {
            Thread.sleep((2 + new Random().nextInt(3)) * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        VerificationStatus result = new Random().nextBoolean()
            ? VerificationStatus.VERIFIED
            : VerificationStatus.REJECTED;

        verificationService.resolveVerification(userId, result);
    }
}
