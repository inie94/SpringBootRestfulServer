package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.repositories.MessagesRepository;

import javax.print.attribute.DateTimeSyntax;
import javax.xml.crypto.Data;
import java.sql.Date;
import java.time.LocalDate;
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
        return repository.findByTopicAndCreatedByBetweenOrderByCreatedByAsc(topic, new Date(new java.util.Date().getTime() - (2 * 24 * 60 * 60 * 1000)), new Date(new java.util.Date().getTime()));
    }
}
