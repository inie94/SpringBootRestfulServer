package ru.inie.social.server.dto;

import lombok.*;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicStatus;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private Long id;
    private TopicStatus status;
    private Set<UserDTO> subscribers;
    private User creator;
    private Set<UserDTO> unsubscribes;
    private String name;
    private Long updatedBy;
}
