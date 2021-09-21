package ru.inie.social.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    List<Message> findByTopicAndCreatedByAfterOrderByCreatedByAsc(Topic topic, Long timestamp);
}
