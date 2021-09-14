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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<User> searchUsers(@RequestParam("value") String searchValue) {
        return userService.searchUsersBy(searchValue);
    }

    @GetMapping("/user/id:{id}/connect")
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
}
