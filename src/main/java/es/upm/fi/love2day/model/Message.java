package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.LocalTime;

@Entity
@Table(name = "Messages")  
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false, unique = true)
    private Long senderId;

    @Column(nullable = false, unique = true)
    private String content;

    @Column(nullable = false)
    private MessageType type;

    @Column(nullable = false)
    private MessageStatus status;
    
    @Column(nullable = false)
    private LocalTime createdAt;
	
	// necesario para JPA
	public Message() {}

    //TODO:Añadir los otros campos
    public Message(Long id, Long senderId, String content, MessageType type) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.type = type;
        this.createdAt = LocalTime.now();
        this.status = MessageStatus.PENDING;    //TODO:
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalTime getCreatedAt() {
        return createdAt;
    }
}


