package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.List;

import java.time.LocalTime;

@Entity
@Table(name = "Chats")  
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatId;

    @Column(nullable = false, unique = true)
    private Long matchId;

    @Column(nullable = false, unique = true)
    private Acount user;

    @Column(nullable = false)
    private List<Message> messages;
	
	// necesario para JPA
	public Chat() {}

    public Chat(Long chatId, Long matchId, Acount user) {
        this.chatId = chatId;
        this.matchId = matchId;
        this.user = user;
        this.messages = new ArrayList<>();      //TODO: Qué estructura?
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public Acount getUser() {
        return user;
    }

    public List<Message> getMessages() {
        return messages;
    }

}
