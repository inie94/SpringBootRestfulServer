package ru.inie.social.server.dto;

import lombok.*;
import ru.inie.social.server.entities.enums.UserStatus;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String password;
    private Date dateOfBirth;
    private Set<RelationshipDTO> relationships;
    private UserStatus status;
}
