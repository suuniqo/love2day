package es.upm.fi.love2day.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import es.upm.fi.love2day.service.MessageWebSocketHandler;
import es.upm.fi.love2day.service.VerificationWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final VerificationWebSocketHandler verificationHandler;
    private final MessageWebSocketHandler messageHandler;

    public WebSocketConfig(
        VerificationWebSocketHandler verificationHandler,
        MessageWebSocketHandler messageHandler
    ) {
        this.verificationHandler = verificationHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(verificationHandler, "/ws/verification").setAllowedOrigins("*");
        registry.addHandler(messageHandler, "/ws/messages").setAllowedOrigins("*");
    }
}
