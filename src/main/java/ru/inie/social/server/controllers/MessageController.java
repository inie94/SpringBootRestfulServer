package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.services.DTOService;
import ru.inie.social.server.services.MessagesService;
import ru.inie.social.server.services.TopicService;
import ru.inie.social.server.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
public class MessageController {

    private final MessagesService messagesService;
    private final TopicService topicService;
    private final UserService userService;

    @Autowired
    public MessageController(MessagesService messagesService, TopicService topicService, UserService userService) {
        this.messagesService = messagesService;
        this.topicService = topicService;
        this.userService = userService;
    }

    @GetMapping("/user/topic/id:{id}/messages")
    public List<MessageDTO> getAllTopicMessages(@PathVariable("id") long topicId) {
        Topic topic = topicService.findById(topicId);
        List<MessageDTO> messages = new ArrayList<>();
        messagesService.getLastMessages(topic).forEach(message -> messages.add(DTOService.toMessageDTO(message)));

        return messages;
    }

    @GetMapping("/user/topic/id:{id}/messages/received")
    public void allMessagesFromTopicReceivedByUser(@PathVariable("id") long id,
                                                    Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Topic topic = topicService.findById(id);

        Set<Message> messages = messagesService.getAllMessagesByTopicAndSenderNot(topic, user);
        Iterator<Message> messageIterator = messages.iterator();
        while(messageIterator.hasNext()) {
            if (messageIterator.next().getReceived().contains(user)) {
                messageIterator.remove();
            }
        }
        messages.forEach(message -> {
            message.getReceived().add(user);
            messagesService.save(message);
        });

    }

    @GetMapping("/user/topic/id:{id}/messages/received/count")
    public int countAllNewMessagesFromTopicReceivedByUser(@PathVariable("id") long id,
                                                            Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Topic topic = topicService.findById(id);

        Set<Message> messages = messagesService.getAllMessagesByTopicAndSenderNot(topic, user);
        Iterator<Message> messageIterator = messages.iterator();
        while(messageIterator.hasNext()) {
            if (messageIterator.next().getReceived().contains(user)) {
                messageIterator.remove();
            }
        }
        return messages.size();
    }
}
