package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.dto.TopicDTO;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.Relationship;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicStatus;
import ru.inie.social.server.services.DTOService;
import ru.inie.social.server.services.TopicService;
import ru.inie.social.server.services.UserService;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

        user.getRelationships().forEach(UserRelationship -> {
            switch (UserRelationship.getTopic().getStatus()) {
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
    public TopicDTO connectToUserTopic(@PathVariable("id") long id,
                                       Principal principal) {
        User user = userService.findByEmail(principal.getName());
        User companion = userService.findById(id);

        AtomicReference<Topic> topic = new AtomicReference<>(new Topic());
        user.getRelationships().forEach(userRelationship -> {
            userRelationship.getTopic().getRelationships().forEach(topicRelationship -> {
                if (topicRelationship.getUser().getId() == companion.getId()) {
                    topic.set(topicRelationship.getTopic());
                }
            });
        });

        /*
         Продолжить исправление
         */

//        Set<Topic> userTopics = topicService.getAllBySubscribersOrUnsubscribesAndStatus(user, user, TopicStatus.PRIVATE);
//        if (userTopics.stream().anyMatch(tpc -> tpc.getUnsubscribes().contains(companion) || tpc.getSubscribers().contains(companion))) {
//            return DTOService.toTopicDTO(userTopics.stream().filter(topic -> topic.getSubscribers().contains(companion) || topic.getUnsubscribes().contains(companion)).findFirst().get());
//        }

//        Set<Topic> companionTopics = topicService.getAllBySubscribersAndUnsubscribes(companion, companion, TopicStatus.PRIVATE);
//        if (companionTopics.stream().anyMatch(topic -> topic.getSubscribers().contains(user) || topic.getUnsubscribes().contains(user))) {
//            return DTOService.toTopicDTO(companionTopics.stream().filter(topic -> topic.getSubscribers().contains(user) || topic.getUnsubscribes().contains(user)).findFirst().get());
//        }


        topic.get().setStatus(TopicStatus.PRIVATE);
        topic.setSubscribers(
                new HashSet<>(
                        Arrays.asList(user)
                )
        );
        topic.setUnsubscribes(
                new HashSet<>(
                        Arrays.asList(companion)
                )
        );
            /**
             * Эта хня наверное не нужна,
             * продумать как реализовать отправку сообщения юзеру
             * у которого еще нет беседы с тобой чтоб он в онлайне увидел
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
        return DTOService.toTopicDTO(topicService.create(topic.get()));
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

    @GetMapping("/user/id:{userId}/topic:{topicId}/subscribe")
    public TopicDTO subscribeAtTopic(@PathVariable("topicId") long topicId,
                                     @PathVariable("userId") long userId) {
        User user = userService.findById(userId);
        Topic topic = topicService.findById(topicId);

        topic.getUnsubscribes().remove(user);
        topic.getSubscribers().add(user);

        return DTOService.toTopicDTO(topicService.update(topic));
    }
}
