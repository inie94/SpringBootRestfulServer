package ru.inie.social.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.inie.social.server.entities.enums.UserStatus;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "topics_subscribers",
            joinColumns = @JoinColumn(name = "subscribers_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> channels;

    @Column(nullable = false)
    private UserStatus status;

    public User() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Topic> getChannels() {
        return channels;
    }

    public void setChannels(Set<Topic> channels) {
        this.channels = channels;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(email, user.email) && Objects.equals(gender, user.gender) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, gender, dateOfBirth, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", channels=" + channels +
                ", status=" + status +
                '}';
    }
}
