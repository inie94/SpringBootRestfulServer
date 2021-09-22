package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicStatus;
import ru.inie.social.server.repositories.TopicRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public Topic findById(Long id) {
        return repository.findById(id).get();
    }

    public Set<Topic> getTopicsBySubscriber(User subscriber) {
        return new HashSet<>(repository.findAllBySubscribers(subscriber));
    }
    public Set<Topic> getTopicsBySubscriber(User subscriber, TopicStatus status) {
        return new HashSet<>(repository.findAllBySubscribersAndStatus(subscriber, status));
    }

    public void create(Topic topic) {
        topic.setUpdatedBy(new Date().getTime());
        repository.save(topic);
    }

    public Topic update(Topic topic) {
        topic.setUpdatedBy(new Date().getTime());
        repository.save(topic);
        return topic;
    }
}
