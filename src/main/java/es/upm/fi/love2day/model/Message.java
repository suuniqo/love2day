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
	public Account() {}
	
    //TODO: Constructor y getters y setters
}


