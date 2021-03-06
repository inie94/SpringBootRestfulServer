package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.repositories.MessagesRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    public void save(Message message) {
        repository.save(message);
    }

    public void save(MessageDTO message) {
        Topic topic = topicService.update(topicService.findById(message.getTopic().getId()));
        message.setTopic(DTOService.toTopicDTO(topic));

        repository.save(toMessage(message));
    }

    public List<Message> getLastMessages(Topic topic) {
        return repository.findFirst20ByTopicOrderByCreatedByDesc(topic).stream()
                .sorted(Comparator.comparingLong(Message::getCreatedBy))
                .collect(Collectors.toList());
    }

    public Message toMessage(MessageDTO dto) {
        Topic topic = topicService.findById(dto.getTopic().getId());
        User sender = userService.findById(dto.getSender().getId());
        Set<User> received = new HashSet<>();
//        dto.getReceived().forEach(userDTO -> received.add(userService.findById(userDTO.getId())));
        return new Message(dto.getId(), dto.getType(), topic, dto.getContent(), sender, dto.getCreatedBy());
    }


//    public List<Message> findAllByTopicAndReceivedNotUser(Topic topic, User user){
//        return repository.findAllByTopicAndReceivedNotIn(topic, new HashSet<User>(Arrays.asList(user)));
//    }

    public Set<Message> getAllMessagesByTopicAndSenderNot(Topic topic, User user){
        return new HashSet<>(repository.findAllByTopicAndSenderNot(topic, user));
    }
}
