package ru.inie.social.server.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column
//    @NotEmpty(message = "Email should not be empty")
//    @Email(message = "Email should be valid")
    private String email;

    @Column
    private String gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column
//    @Size(min = 6, max = 12, message = "Password should be between 6 and 12 characters")
    private String password;

//    @ManyToMany
//    @JoinTable(
//            name = "subscriber_channels",
//            joinColumns = {@JoinColumn(name = "channels_id")}
//    )
//    private Set<Topic> channels;

//    @Column(name = "group_id")
//    private String groupId;

//    @ManyToMany
//    @JoinTable(
//            name = "user_subscriptions",
//            joinColumns = {@JoinColumn(name = "channel_id")},
//            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
//    )
//    private Set<User> subscribers = new HashSet<>();
//
//    @ManyToMany
//    @JoinTable(
//            name = "user_subscriptions",
//            joinColumns = {@JoinColumn(name = "subscriber_id")},
//            inverseJoinColumns = {@JoinColumn(name = "channel_id")}
//    )
//    private Set<User> subscriptions = new HashSet<>();

//    @OneToMany(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Project> projects;

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
}
