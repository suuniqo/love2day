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
@Table(name = "Messages")  
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private String mediaKind;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;
	
	// necesario para JPA
	public Message() {}

    private Message(
        Long senderId,
        Long matchId,
        String type,
        String content,
        Instant createdAt,
        MessageStatus status
    ) {
        this.senderId = senderId;
        this.matchId = matchId;
        this.mediaKind = type;
        this.content = content;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static Message create(
        Long senderId,
        Long matchId,
        String type,
        String content
    ) {
        return new Message(
            senderId,
            matchId,
            type,
            content,
            Instant.now(),
            MessageStatus.SENDING
        );
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public String getMediaKind() {
        return mediaKind;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public MessageStatus getStatusAs(Long userId) {
        if (userId == senderId) {
            return MessageStatus.READ;
        }

        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public boolean isReadBy(Long userId) {
        return getStatusAs(userId) == MessageStatus.READ;
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
    }
}
