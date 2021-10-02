package ru.inie.social.server.services;

import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.dto.RelationshipDTO;
import ru.inie.social.server.dto.TopicDTO;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Relationship;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;

import java.util.HashSet;
import java.util.Set;

public class DTOService {

    public static UserDTO toUserDTO(User user) {
        Set<RelationshipDTO> relationships = new HashSet<>();

        if(user.getRelationships() != null)
            user.getRelationships().forEach(relationship -> relationships.add(toRelationshipDTO(relationship)));

        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .gender(user.getGender())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .relationships(relationships)
                .build();
    }

    private static RelationshipDTO toRelationshipDTO(Relationship relationship) {
        return RelationshipDTO.builder()
                .id(relationship.getId())
                .status(relationship.getStatus())
                .topic(toTopicDTO(relationship.getTopic()))
                .user(toUserDTOWithoutRelationships(relationship.getUser()))
                .updatedBy(relationship.getUpdatedBy())
                .build();
    }

    public static TopicDTO toTopicDTO(Topic topic) {
        Set<RelationshipDTO> relationships = new HashSet<>();

        if(topic.getRelationships() != null)
            topic.getRelationships().forEach(relationship -> relationships.add(toRelationshipDTO(relationship)));

        return TopicDTO.builder()
                .id(topic.getId())
                .creator(topic.getCreator())
                .name(topic.getName())
                .status(topic.getStatus())
                .relationships(relationships)
                .updatedBy(topic.getUpdatedBy())
                .build();
    }

    public static UserDTO toUserDTOWithoutRelationships(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .gender(user.getGender())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .status(user.getStatus())
                .build();
    }

    public static MessageDTO toMessageDTO(Message message) {

        return MessageDTO.builder()
                .id(message.getId())
                .type(message.getType())
                .topic(toTopicDTO(message.getTopic()))
                .sender(toUserDTOWithoutRelationships(message.getSender()))
                .content(message.getContent())
                .createdBy(message.getCreatedBy())
                .build();
    }
}
