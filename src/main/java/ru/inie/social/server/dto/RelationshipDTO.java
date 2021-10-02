package ru.inie.social.server.dto;

import lombok.*;
import ru.inie.social.server.entities.enums.SubscribeStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipDTO {
        private Long id;
        private TopicDTO topic;
        private UserDTO user;
        private SubscribeStatus status;
        private Long updatedBy;
}
