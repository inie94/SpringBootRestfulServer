package ru.inie.social.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.inie.social.server.entities.Topic;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findById(Long id);

//    List<Topic> findAllBySubscribers(User subscriber);

//    List<Topic> findAllBySubscribersAndUnsubscribesAndStatus(User subscriber, User unsubscribe, TopicStatus status);

//    List<Topic> findAllBySubscribersOrUnsubscribesAndStatus(User subscriber, User unsubscribe, TopicStatus status);

//    Optional<Topic> findBySubscribersIdAndUnsubscribesIdAndStatus(Long subscriberId, Long unsubscribeId, TopicStatus status);

//    List<Topic> findAllBySubscribersAndStatus(User subscriber, TopicStatus status);



}
