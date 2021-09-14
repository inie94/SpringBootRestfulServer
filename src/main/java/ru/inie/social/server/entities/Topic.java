package ru.inie.social.server.entities;

import ru.inie.social.server.entities.enums.TopicStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private TopicStatus status;

    @ManyToMany
//    @JoinTable(
//            name = "user_subscriptions",
//            joinColumns = {@JoinColumn(name = "channel_id")}
//    )
    private Set<User> subscriber;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column
    private String name;

    public Topic() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }

    public Set<User> getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Set<User> subscriber) {
        this.subscriber = subscriber;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(id, topic.id) && status == topic.status && Objects.equals(creator, topic.creator) && Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, creator, name);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", status=" + status +
                ", creator=" + creator +
                ", name='" + name + '\'' +
                '}';
    }
}
