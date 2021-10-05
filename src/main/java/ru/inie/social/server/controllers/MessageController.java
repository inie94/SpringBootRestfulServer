package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.dto.RelationshipDTO;
import ru.inie.social.server.entities.Relationship;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.services.*;

import java.security.Principal;
import java.util.*;

@RestController
public class MessageController {

    private final MessagesService messagesService;
    private final TopicService topicService;
    private final UserService userService;
    private final RelationshipsService relationshipsService;

    @Autowired
    public MessageController(MessagesService messagesService, TopicService topicService, UserService userService, RelationshipsService relationshipsService) {
        this.messagesService = messagesService;
        this.topicService = topicService;
        this.userService = userService;
        this.relationshipsService = relationshipsService;
    }

    @GetMapping("/user/topic/id:{id}/messages")
    public List<MessageDTO> getAllTopicMessages(@PathVariable("id") long topicId) {
        Topic topic = topicService.findById(topicId);
        List<MessageDTO> messages = new ArrayList<>();
        messagesService.getLastMessages(topic).forEach(message -> messages.add(DTOService.toMessageDTO(message)));

        return messages;
    }

    @GetMapping("/user/topic/id:{id}/messages/received")
    public RelationshipDTO allMessagesFromTopicReceivedByUser(@PathVariable("id") long id,
                                                              Principal principal) {
        User user = userService.findByEmail(principal.getName());

        Relationship userRelationship =
                user.getRelationships().stream()
                        .filter(relationship -> relationship.getTopic().getId() == id)
                        .findFirst()
                        .get();

        userRelationship.setUpdatedBy(new Date().getTime());

        return DTOService.toRelationshipDTOWithoutUser(relationshipsService.save(userRelationship));
    }

//    @GetMapping("/user/topic/id:{id}/messages/received/count")
//    public int countAllNewMessagesFromTopicReceivedByUser(@PathVariable("id") long id,
//                                                            Principal principal) {
//        User user = userService.findByEmail(principal.getName());
//        Topic topic = topicService.findById(id);
//
//        Set<Message> messages = messagesService.getAllMessagesByTopicAndSenderNot(topic, user);
//        Iterator<Message> messageIterator = messages.iterator();
//        while(messageIterator.hasNext()) {
//            if (messageIterator.next().getReceived().contains(user)) {
//                messageIterator.remove();
//            }
//        }
//        return messages.size();
//    }
}
