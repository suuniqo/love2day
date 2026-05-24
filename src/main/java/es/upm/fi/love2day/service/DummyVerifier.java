package es.upm.fi.love2day.service;

import es.upm.fi.love2day.interfaces.Verifier;
import es.upm.fi.love2day.model.VerificationInquiry;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DummyVerifier implements Verifier {
    // Se tiene que usar ya que si se llamase en la misma clase,
    // como no atraviesa el proxy de SpringBoot, sería síncrona.
    private final DummyVerifierAsyncHelper asyncHelper;

    public DummyVerifier(@Lazy DummyVerifierAsyncHelper asyncHelper) {
        this.asyncHelper = asyncHelper;
    }

    @Override
    public VerificationInquiry createInquiry(Long userId) {
        asyncHelper.scheduleResolution(userId);

        return new VerificationInquiry(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );
    }
}
