package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.dto.RelationshipDTO;
import ru.inie.social.server.dto.TopicDTO;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.Relationship;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.SubscribeStatus;
import ru.inie.social.server.entities.enums.TopicMode;
import ru.inie.social.server.services.DTOService;
import ru.inie.social.server.services.RelationshipsService;
import ru.inie.social.server.services.TopicService;
import ru.inie.social.server.services.UserService;

import java.security.Principal;
import java.util.*;

@RestController
public class UserMovesController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final TopicService topicService;
    private final RelationshipsService relationshipsService;

    @Autowired
    public UserMovesController(SimpMessagingTemplate messagingTemplate, UserService userService, TopicService topicService, RelationshipsService relationshipsService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.topicService = topicService;
        this.relationshipsService = relationshipsService;
    }

    @GetMapping("/search")
    public List<UserDTO> searchUsers(@RequestParam("value") String searchValue, Principal principal) {

        User user = userService.findByEmail(principal.getName());

        List<UserDTO> usersDTOs = new ArrayList<>();
        List<User> users = userService.searchUsersBy(searchValue);

        user.getRelationships().forEach(UserRelationship -> {
            switch (UserRelationship.getTopic().getMode()) {
                case PRIVATE:
                    UserRelationship.getTopic().getRelationships().forEach(topicRelationships -> {
                        User subscriber = topicRelationships.getUser();
                        if (subscriber.getId() != user.getId()) {
                            if (subscriber.getEmail().startsWith(searchValue) ||
                                subscriber.getFirstname().startsWith(searchValue) ||
                                subscriber.getLastname().startsWith(searchValue)) {

                                users.remove(subscriber);
                            }
                        }
                    });
                    break;
                case PUBIC:
            }
        });

        users.remove(user);

        users.forEach(user1 -> usersDTOs.add(DTOService.toUserDTOWithoutRelationships(user1)));

        return usersDTOs;
    }

    @GetMapping("/user/id:{id}/get-topic")
    public RelationshipDTO connectToUserTopic(@PathVariable("id") long id,
                                              Principal principal) {
        User user = userService.findByEmail(principal.getName());
        User companion = userService.findById(id);

        Relationship userRelationship;
        Relationship companionRelationship;

        List<Relationship> relationships = relationshipsService.getRelationshipByUserAndUserAndTopicStatus(user, companion, TopicMode.PRIVATE);
        if (relationships != null) {
            userRelationship = relationships.stream().filter(relationship -> relationship.getUser().equals(user)).findFirst().get();
            companionRelationship = relationships.stream().filter(relationship -> relationship.getUser().equals(companion)).findFirst().get();
            if(userRelationship.getStatus() != SubscribeStatus.SUBSCRIBE){
                userRelationship.setStatus(SubscribeStatus.SUBSCRIBE);
            }
            if(companionRelationship.getStatus() == null)
                companionRelationship.setStatus(SubscribeStatus.UNSUBSCRIBE);

            userRelationship = relationshipsService.save(userRelationship);
            relationshipsService.save(companionRelationship);

            return DTOService.toRelationshipDTO(userRelationship);
        }

        Topic topic = new Topic();
        topic.setMode(TopicMode.PRIVATE);
        topic = topicService.create(topic);


        userRelationship = new Relationship();
        userRelationship.setUser(user);
        userRelationship.setTopic(topic);
        companionRelationship = new Relationship();
        companionRelationship.setUser(companion);
        companionRelationship.setTopic(topic);

        userRelationship = relationshipsService.save(userRelationship);
        relationshipsService.save(companionRelationship);

        return DTOService.toRelationshipDTO(userRelationship);
    }

//    @GetMapping("/user/topics")
//    public Set<Topic> getAllUserTopics(Principal principal) {
//        User user = userService.findByEmail(principal.getName());
//        return topicService.getTopicsBySubscriber(user);
//    }

    @GetMapping("/user/topic:{id}")
    public Topic getTopic(@PathVariable("id") long id) {
        return topicService.findById(id);
    }

    @GetMapping("/user/topic:{id}/unsubscribe")
    public void unsubscribeAtTopic(@PathVariable("id") long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Topic topic = topicService.findById(id);

//        topic.getSubscribers().remove(user);
////        topic.getUnsubscribes().add(user);
//
//        if (topic.getSubscribers().isEmpty()) {
//            // mb delete topic
//        }

        topicService.update(topic);
    }

    @GetMapping("/user/id:{userId}/topic:{topicId}/subscribe")
    public TopicDTO subscribeAtTopic(@PathVariable("topicId") long topicId,
                                     @PathVariable("userId") long userId) {
        User user = userService.findById(userId);
        Topic topic = topicService.findById(topicId);

//        topic.getUnsubscribes().remove(user);
//        topic.getSubscribers().add(user);

        return DTOService.toTopicDTO(topicService.update(topic));
    }
}
