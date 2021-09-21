package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.repositories.MessagesRepository;

import javax.print.attribute.DateTimeSyntax;
import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class MessagesService {

    private final MessagesRepository repository;

    @Autowired
    public MessagesService(MessagesRepository repository) {
        this.repository = repository;
    }

    public void save(Message message) {
        repository.save(message);
    }

    public List<Message> getLastMessages(Topic topic) {
        Long moment = new Date().getTime();
        return repository.findByTopicAndCreatedByAfterOrderByCreatedByAsc(topic, moment - (2 * 24 * 60 * 60 * 1000));
    }
}
