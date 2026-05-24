package es.upm.fi.love2day.interfaces;

import es.upm.fi.love2day.model.VerificationInquiry;

public interface Verifier {
    VerificationInquiry createInquiry(Long userId);
}
