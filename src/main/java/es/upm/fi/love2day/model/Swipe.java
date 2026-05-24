package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.Instant;

@Entity
@Table(name = "Swipes")  
public class Swipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sourceId;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwipeType type;

    @Column(nullable = false)
    private Instant createdAt;
    
    // necesario para JPA
    public Swipe() {}

    private Swipe(Long sourceId, Long targetId, SwipeType type, Instant createdAt) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.type = type;
        this.createdAt = createdAt;
    }

    public static Swipe create(Long sourceId, Long targetId, SwipeType type) {
        return new Swipe(sourceId, targetId, type, Instant.now());
    }

    public Long getId() {
        return id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public SwipeType getType() {
        return type;
    }

    public void setType(SwipeType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
