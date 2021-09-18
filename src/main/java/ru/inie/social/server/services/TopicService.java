package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.repositories.TopicRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public Topic findById(Long id) {
        return repository.findById(id).get();
    }

    public Set<Topic> getTopicsBySubscriber(User subscriber) {
        return new HashSet<>(repository.findAllBySubscriber(subscriber));
    }

    public void create(Topic topic) {
        repository.save(topic);
    }
}
