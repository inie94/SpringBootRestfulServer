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

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "topic")
    private Set<Relationship> relationships;
    
    private String name;

    @Column(nullable = false)
    private Long updatedBy;
}
