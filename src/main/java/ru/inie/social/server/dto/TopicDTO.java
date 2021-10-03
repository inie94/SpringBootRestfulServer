package ru.inie.social.server.dto;

import lombok.*;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicMode;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private Long id;
    private TopicMode mode;
    private Set<RelationshipDTO> relationships;
    private User creator;
    private String name;
    private Long updatedBy;
}
