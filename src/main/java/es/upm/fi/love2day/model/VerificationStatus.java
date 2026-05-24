package es.upm.fi.love2day.model;

public enum VerificationStatus {
    UNVERIFIED,
    PENDING,
    VERIFIED,
    REJECTED;

    public boolean isVerified() {
        return this == VERIFIED;
    }
}
