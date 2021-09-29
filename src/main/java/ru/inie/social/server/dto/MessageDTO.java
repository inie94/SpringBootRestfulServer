package ru.inie.social.server.dto;

import lombok.*;
import ru.inie.social.server.entities.enums.MessageType;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private MessageType type;
    private TopicDTO topic;
    private String content;
    private UserDTO sender;
    private Long createdBy;
    private Set<UserDTO> received;
}
