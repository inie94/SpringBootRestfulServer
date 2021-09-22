package ru.inie.social.server.entities;

import lombok.*;
import ru.inie.social.server.entities.enums.TopicStatus;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private TopicStatus status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "topics_subscribers",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribers_id")
    )
    private Set<User> subscribers;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany
    private Set<User> unsubscribes;
    
    private String name;

    @Column(nullable = false)
    private Long updatedBy;
}
