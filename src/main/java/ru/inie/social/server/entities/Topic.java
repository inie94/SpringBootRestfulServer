package ru.inie.social.server.entities;

import ru.inie.social.server.entities.enums.TopicStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private TopicStatus status;

    @ManyToMany
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
        return subscribers;
    }

    public void setSubscriber(Set<User> subscribers) {
        this.subscribers = subscribers;
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

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<User> getUnsubscribes() {
        return unsubscribes;
    }

    public void setUnsubscribes(Set<User> unsubscribes) {
        this.unsubscribes = unsubscribes;
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
                ", subscribers=" + subscribers +
                ", creator=" + creator +
                ", unsubscribes=" + unsubscribes +
                ", name='" + name + '\'' +
                '}';
    }
}
