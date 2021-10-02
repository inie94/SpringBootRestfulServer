package ru.inie.social.server.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.inie.social.server.entities.enums.UserStatus;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name",
            nullable = false)
    private String firstname;

    @Column(name = "last_name",
            nullable = false)
    private String lastname;

    @Column(nullable = false)
//    @NotEmpty(message = "Email should not be empty")
//    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false)
//    @Size(min = 6, max = 12, message = "Password should be between 6 and 12 characters")
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Relationship> relationships;

    @Column(nullable = false)
    private UserStatus status;
}
