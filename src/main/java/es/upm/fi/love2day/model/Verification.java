package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Verifications")  
public class Verification {
	@Id
    @Column(nullable = false, unique = true)
	private Long userId;

    @Column(nullable = false)
    private VerificationStatus status;

    // necesario para JPA
	public Verification() {}
	
	public Verification(Long userId, VerificationStatus status) {
        this.userId = userId;
        this.status = status;
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
