package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.services.DTOService;
import ru.inie.social.server.services.MessagesService;
import ru.inie.social.server.services.TopicService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {

    private final MessagesService messagesService;
    private final TopicService topicService;

    @Autowired
    public MessageController(MessagesService messagesService, TopicService topicService) {
        this.messagesService = messagesService;
        this.topicService = topicService;
    }

    @GetMapping("/user/topic/id:{id}/messages")
    public List<MessageDTO> getAllTopicMessages(@PathVariable("id") long topicId) {
        Topic topic = topicService.findById(topicId);
        List<MessageDTO> messages = new ArrayList<>();
        messagesService.getLastMessages(topic).forEach(message -> messages.add(DTOService.toMessageDTO(message)));

        return messages;
    }
}
