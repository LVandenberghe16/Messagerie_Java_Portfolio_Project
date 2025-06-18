package com.messagerie.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonBackReference(value = "user-messages")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    @JsonBackReference(value = "channel-messages")
    private Channel channel;

    public Message() {}

    public Message(String content, User sender, Channel channel) {
        this.content = content;
        this.sender = sender;
        this.channel = channel;
        this.timestamp = LocalDateTime.now(); // Timestamp au moment de l'envoi
    }

    // Getters & setters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
