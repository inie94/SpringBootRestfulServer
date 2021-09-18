package ru.inie.social.server.entities;

import ru.inie.social.server.entities.enums.MessageType;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private Timestamp createdBy;

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Timestamp getCreatedBy() {
        return createdBy;

    }

    public void setCreatedBy(Timestamp createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && type == message.type && Objects.equals(topic, message.topic) && Objects.equals(content, message.content) && Objects.equals(sender, message.sender) && Objects.equals(createdBy, message.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, topic, content, sender, createdBy);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", type=" + type +
                ", topic=" + topic +
                ", content='" + content + '\'' +
                ", sender=" + sender +
                ", createdBy=" + createdBy +
                '}';
    }
}
