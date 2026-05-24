package es.upm.fi.love2day.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VerificationInquiry {
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String sessionToken;

    // Required by JPA
    protected VerificationInquiry() {}

    public VerificationInquiry(String id, String sessionToken) {
        this.id = id;
        this.sessionToken = sessionToken;
    }

    public String getId() {
        return id;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
