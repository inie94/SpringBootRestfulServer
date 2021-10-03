package ru.inie.social.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.inie.social.server.entities.Relationship;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicMode;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipsRepository extends JpaRepository<Relationship, Long> {

    @Query("SELECT r FROM Relationship r INNER JOIN Topic t ON t = r.topic " +
            "WHERE t.mode = ?3 AND (r.user = ?1 OR r.user = ?2) " +
            "AND r.topic IN (SELECT topic FROM Relationship GROUP BY topic HAVING COUNT(topic) > 1)")
    List<Relationship> findAllByUserOrCompanionAndTopicMode(User user, User companion, TopicMode mode);
}
