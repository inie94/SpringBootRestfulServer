package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inie.social.server.entities.Relationship;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicMode;
import ru.inie.social.server.repositories.RelationshipsRepository;

import java.util.List;

@Service
public class RelationshipsService {

    private final RelationshipsRepository relationshipsRepository;

    @Autowired
    public RelationshipsService(RelationshipsRepository relationshipsRepository) {
        this.relationshipsRepository = relationshipsRepository;
    }

    public Relationship save(Relationship relationship) {
        return relationshipsRepository.save(relationship);
    }

    public List<Relationship> getRelationshipByUserAndUserAndTopicStatus(User user, User companion, TopicMode topicMode) {
        return relationshipsRepository.findAllByUserOrCompanionAndTopicMode(user, companion, topicMode);
    }
}
