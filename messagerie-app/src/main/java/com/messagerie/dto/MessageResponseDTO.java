package com.messagerie.dto;

import java.time.LocalDateTime;

public class MessageResponseDTO {
    private String content;
    private String senderUsername;
    private LocalDateTime timestamp;

    public MessageResponseDTO(String content, String senderUsername, LocalDateTime timestamp) {
        this.content = content;
        this.senderUsername = senderUsername;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
