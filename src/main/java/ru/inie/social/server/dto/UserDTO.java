package ru.inie.social.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.inie.social.server.entities.Topic;
import ru.inie.social.server.entities.enums.UserStatus;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
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
    private Set<TopicDTO> channels;
    private Set<Long> channelsId;
    private UserStatus status;
}
