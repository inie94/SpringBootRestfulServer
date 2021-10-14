package ru.inie.social.server.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;

import java.util.List;
import java.util.Set;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {

    List<Message> findFirst20ByTopicOrderByCreatedByDesc(Topic topic);

    List<Message> findByTopicAndCreatedByAfterOrderByCreatedByAsc(Topic topic, Long timestamp);

    List<Message> findAllByTopicAndSenderNot(Topic topic, User user);

}
