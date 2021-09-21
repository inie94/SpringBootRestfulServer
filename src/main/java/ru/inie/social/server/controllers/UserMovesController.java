package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicStatus;
import ru.inie.social.server.services.TopicService;
import ru.inie.social.server.services.UserService;

import java.security.Principal;
import java.util.*;

@RestController
public class UserMovesController {

    private final UserService userService;
    private final TopicService topicService;

    @Autowired
    public UserMovesController(UserService userService, TopicService topicService) {
        this.userService = userService;
        this.topicService = topicService;
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam("value") String searchValue, Principal principal) {
        User user = userService.findByEmail(principal.getName());

        List<User> users = userService.searchUsersBy(searchValue);


        user.getChannels().forEach(topic -> {
            switch (topic.getStatus()) {
                case PRIVATE:
                    topic.getSubscriber().forEach(subscriber -> {
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
        return users;
    }

    @GetMapping("/user/id:{id}/get-topic")
    public Topic connectToUserTopic(@PathVariable("id") long id,
                                   Principal principal) {
        User user = userService.findByEmail(principal.getName());
        User companion = userService.findById(id);

        Set<Topic> topics = topicService.getTopicsBySubscriber(user);

        if (topics.isEmpty() || !topics.stream().anyMatch(item -> item.getSubscriber().contains(companion))) {
            Topic topic = new Topic();
            topic.setStatus(TopicStatus.PRIVATE);
            Set<User> subscribers = new HashSet<>();
            subscribers.addAll(Arrays.asList(user, companion));
            topic.setSubscriber(subscribers);
            topicService.create(topic);

            return topic;
        } else {
            return topics.stream().filter(item -> item.getSubscriber().contains(companion)).findFirst().get();
        }
    }

    @GetMapping("/user/topics")
    public Set<Topic> getAllUserTopics(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return topicService.getTopicsBySubscriber(user);
    }

    @GetMapping("/user/topic:{id}")
    public Topic getTopics(@PathVariable("id") long id) {
        return topicService.findById(id);
    }
}
