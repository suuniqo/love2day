package es.upm.fi.love2day.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Verifications")  
public class Verification {
	@Id
	private Long userId;

    @Column(nullable = false)
    private VerificationInquiry inquiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status;

    // necesario para JPA
	public Verification() {}
	
	private Verification(Long userId, VerificationInquiry inquiry, VerificationStatus status) {
        this.userId = userId;
        this.inquiry = inquiry;
        this.status = status;
    }

	public static Verification create(Long userId, VerificationInquiry inquiry) {
        return new Verification(userId, inquiry, VerificationStatus.PENDING);
    }

    public Long getUserId() {
        return userId;
    }

    public VerificationInquiry getInquiry() {
        return inquiry;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }
}
