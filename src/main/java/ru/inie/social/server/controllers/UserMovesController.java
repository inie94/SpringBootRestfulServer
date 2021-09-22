package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.dto.TopicDTO;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.MessageType;
import ru.inie.social.server.entities.enums.TopicStatus;
import ru.inie.social.server.services.DTOService;
import ru.inie.social.server.services.TopicService;
import ru.inie.social.server.services.UserService;

import java.security.Principal;
import java.util.*;

@RestController
public class UserMovesController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final TopicService topicService;

    @Autowired
    public UserMovesController(SimpMessagingTemplate messagingTemplate, UserService userService, TopicService topicService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.topicService = topicService;
    }

    @GetMapping("/search")
    public List<UserDTO> searchUsers(@RequestParam("value") String searchValue, Principal principal) {

        User user = userService.findByEmail(principal.getName());

        List<UserDTO> usersDTOs = new ArrayList<>();
        List<User> users = userService.searchUsersBy(searchValue);

        user.getChannels().forEach(topic -> {
            switch (topic.getStatus()) {
                case PRIVATE:
                    topic.getSubscribers().forEach(subscriber -> {
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

        users.forEach(user1 -> usersDTOs.add(DTOService.toUserDTOWithoutChannels(user1)));

        return usersDTOs;
    }

    @GetMapping("/user/id:{id}/get-topic")
    public TopicDTO connectToUserTopic(@PathVariable("id") long id,
                                       Principal principal) {
        User user = userService.findByEmail(principal.getName());
        User companion = userService.findById(id);

        Set<Topic> topics = topicService.getTopicsBySubscriber(companion, TopicStatus.PRIVATE);

        Topic topic;
        if (topics.isEmpty() ||
            !topics.stream().anyMatch(item -> item.getSubscribers().contains(user)) ||
            !topics.stream().anyMatch(item -> item.getUnsubscribes().contains(user))) {

            topic = new Topic();
            topic.setStatus(TopicStatus.PRIVATE);
            topic.setSubscribers(
                    new HashSet<>(
                            Arrays.asList(user, companion)
                    )
            );

            /**
             * Эта хня наверное не нужна,
             * продумать как реализовать отправку сообщения юзеру
             * у которог еще нет беседы с тобой чтоб он в онлавне увидел
             *
             * messagingTemplate.convertAndSend(
             *      "/topic/notification",
             *      MessageDTO.builder()
             *          .type(MessageType.AWAIT_CONNECTION)
             *          .sender(DTOService.toUserDTO(companion))
             *          .build()
             * );
             *
             */

            topicService.create(topic);


        } else {
            topic = topics.stream().filter(item -> item.getSubscribers().contains(user) || item.getUnsubscribes().contains(user)).findFirst().get();
            if (topic.getUnsubscribes().contains(user)) {
                topic.getSubscribers().add(user);
                topic.getUnsubscribes().remove(user);
                topicService.update(topic);
            }
        }
        return DTOService.toTopicDTO(topic);
    }

    @GetMapping("/user/topics")
    public Set<Topic> getAllUserTopics(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return topicService.getTopicsBySubscriber(user);
    }

    @GetMapping("/user/topic:{id}")
    public Topic getTopic(@PathVariable("id") long id) {
        return topicService.findById(id);
    }

    @GetMapping("/user/topic:{id}/unsubscribe")
    public void unsubscribeAtTopic(@PathVariable("id") long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Topic topic = topicService.findById(id);

        topic.getSubscribers().remove(user);
//        topic.getUnsubscribes().add(user);

        if (topic.getSubscribers().isEmpty()) {
            // mb delete topic
        }

        topicService.update(topic);
    }
}
