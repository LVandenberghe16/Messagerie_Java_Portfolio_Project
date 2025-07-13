package com.messagerie.dto;

import java.time.LocalDateTime;

public class MessageResponseDTO {
    private String content;
    private String senderUsername;
    private Long channelId;
    private LocalDateTime timestamp;

    public MessageResponseDTO(String content, String senderUsername, Long channelId, LocalDateTime timestamp) {
        this.content = content;
        this.senderUsername = senderUsername;
        this.channelId = channelId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Long getChannelId() {
        return channelId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}