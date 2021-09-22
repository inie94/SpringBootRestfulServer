package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.repositories.MessagesRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessagesService {

    private final MessagesRepository repository;
    private final TopicService topicService;
    private final UserService userService;

    @Autowired
    public MessagesService(MessagesRepository repository, TopicService topicService, UserService userService) {
        this.repository = repository;
        this.topicService = topicService;
        this.userService = userService;
    }

    public void save(MessageDTO message) {
        Topic topic = topicService.update(topicService.findById(message.getTopic().getId()));
        message.setTopic(DTOService.toTopicDTO(topic));
        repository.save(toMessage(message));
    }

    public List<Message> getLastMessages(Topic topic) {
        Long moment = new Date().getTime();
        return repository.findByTopicAndCreatedByAfterOrderByCreatedByAsc(topic, moment - (2 * 24 * 60 * 60 * 1000));
    }

    public Message toMessage(MessageDTO dto) {
        Topic topic = topicService.findById(dto.getTopic().getId());
        User sender = userService.findById(dto.getSender().getId());
        return new Message(dto.getId(), dto.getType(), topic, dto.getContent(), sender, dto.getCreatedBy());
    }
}
