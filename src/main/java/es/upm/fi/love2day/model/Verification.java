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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status;

    // necesario para JPA
	public Verification() {}
	
	private Verification(Long userId, VerificationStatus status) {
        this.userId = userId;
        this.status = status;
    }

	public Verification create(Long userId, VerificationStatus status) {
        return new Verification(userId, status);
    }

    public Long getUserId() {
        return userId;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }
}
