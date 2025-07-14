package com.messagerie.dto;

import com.messagerie.model.Message;

import java.time.LocalDateTime;

public class MessageDTO {

    private Long id;
    private String content;
    private Long senderId;
    private String senderUsername;
    private Long channelId;
    private LocalDateTime timestamp;

    public MessageDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Fabrique un DTO depuis l'entité Message
     */
    public static MessageDTO fromEntity(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setContent(m.getContent());
        dto.setSenderId(m.getSender().getId());
        dto.setSenderUsername(m.getSender().getUsername());
        dto.setChannelId(m.getChannel().getId());
        dto.setTimestamp(m.getTimestamp());
        return dto;
    }
}
