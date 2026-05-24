package es.upm.fi.love2day.service;

import es.upm.fi.love2day.model.VerificationStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationWebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        extractUserId(session).ifPresent(userId -> sessions.put(userId, session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        extractUserId(session).ifPresent(sessions::remove);
    }

    public void notify(Long userId, VerificationStatus status) {
        WebSocketSession session = sessions.get(userId);

        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            session.sendMessage(new TextMessage(status.name()));
        } catch (IOException e) {
            sessions.remove(userId);
        }
    }

    private Optional<Long> extractUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();

        if (query == null) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(Long.parseLong(query.replace("userId=", "")));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
