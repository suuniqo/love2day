package es.upm.fi.love2day.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "Chats")  
public class Chat {
    @Id
    private Long matchId;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean isActive;

    // necesario para JPA
	public Chat() {}

	private Chat(Long matchId, Instant createdAt, boolean isActive) {
        this.matchId = matchId;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

	public static Chat create(Long matchId) {
        return new Chat(matchId, Instant.now(), true);
    }

    public Long getMatchId() {
        return matchId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
