package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.inie.social.server.dto.MessageDTO;
import ru.inie.social.server.dto.TopicDTO;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.Message;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.User;

import java.util.HashSet;
import java.util.Set;

public class DTOService {

    public static UserDTO toUserDTO(User user) {
        Set<TopicDTO> channels = new HashSet<>();

        user.getChannels().forEach(topic -> channels.add(toTopicDTO(topic)));

        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .gender(user.getGender())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .channels(channels)
                .build();
    }

    public static TopicDTO toTopicDTO(Topic topic) {
        Set<UserDTO> subscribers = new HashSet<>();
        Set<UserDTO> unsubscribes = new HashSet<>();

        Set<User> topicSubscribers = topic.getSubscribers();
        if (topicSubscribers != null)
            topicSubscribers.forEach(user -> subscribers.add(toUserDTOWithoutChannels(user)));

        Set<User> topicUnsubscribes = topic.getUnsubscribes();
        if (topicUnsubscribes != null)
            topicUnsubscribes.forEach(user -> unsubscribes.add(toUserDTOWithoutChannels(user)));

        return TopicDTO.builder()
                .id(topic.getId())
                .creator(topic.getCreator())
                .name(topic.getName())
                .status(topic.getStatus())
                .subscribers(subscribers)
                .unsubscribes(unsubscribes)
                .updatedBy(topic.getUpdatedBy())
                .build();
    }

    public static UserDTO toUserDTOWithoutChannels(User user) {
        Set<Long> channelsId = new HashSet<>();

//        Set<Topic> topics = user.getChannels();
//        if (topics != null)
//            topics.forEach(topic -> channelsId.add(topic.getId()));

        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .gender(user.getGender())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .status(user.getStatus())
//                .channelsId(channelsId)
                .build();
    }

    public static MessageDTO toMessageDTO(Message message) {

        Set<UserDTO> received = new HashSet<>();
        message.getReceived().forEach(user -> received.add(toUserDTOWithoutChannels(user)));

        return MessageDTO.builder()
                .id(message.getId())
                .type(message.getType())
                .topic(toTopicDTO(message.getTopic()))
                .sender(toUserDTOWithoutChannels(message.getSender()))
                .content(message.getContent())
                .received(received)
                .createdBy(message.getCreatedBy())
                .build();
    }
}
