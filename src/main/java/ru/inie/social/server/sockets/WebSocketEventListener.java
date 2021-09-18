package ru.inie.social.server.sockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.MessageType;
import ru.inie.social.server.entities.enums.UserStatus;
import ru.inie.social.server.services.UserService;

@Component
public class WebSocketEventListener {

    private final UserService service;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebSocketEventListener(UserService service) {
        this.service = service;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        User user1 = (User) headerAccessor.getSessionAttributes().get("username");
        if(user1 != null) {
            User user = disconnectUser(user1.getEmail());
            logger.info("User Disconnected : " + user.getEmail());
            messagingTemplate.convertAndSend("/topic/notification", createLeaveMessage(user));
        }
    }

    private Message createLeaveMessage(User user) {
        Message message = new Message();
        message.setType(MessageType.LEAVE);
        message.setTopic(null);
        message.setSender(user);
        message.setContent(String.valueOf(user.getId()));
        return message;
    }

    private User disconnectUser(String email) {
        User user = service.findByEmail(email);
        user.setStatus(UserStatus.OFFLINE);
        service.save(user);
        return user;
    }
}