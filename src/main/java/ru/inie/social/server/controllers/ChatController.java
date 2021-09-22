package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.services.MessagesService;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessagesService service;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, MessagesService service) {
        this.messagingTemplate = messagingTemplate;
        this.service = service;
    }

    @MessageMapping("/chat.sendMessage")
    public MessageDTO sendMessage(@Payload MessageDTO message) {
        service.save(message);
        messagingTemplate.convertAndSend("/topic/id:" + message.getTopic().getId(), message);
        return message;
    }

    @MessageMapping("/chat.notification")
    @SendTo("/topic/notification")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

}