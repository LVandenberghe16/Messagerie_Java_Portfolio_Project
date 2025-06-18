package com.messagerie.controller;

import com.messagerie.dto.MessageDTO;
import com.messagerie.model.Channel;
import com.messagerie.model.Message;
import com.messagerie.model.User;
import com.messagerie.repository.ChannelRepository;
import com.messagerie.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @MessageMapping("/chat") // Correspond à "/app/chat"
    @SendTo("/topic/messages")
    public Message handleMessage(MessageDTO dto) {
        User sender = userRepository.findById(dto.getSenderId()).orElse(null);
        Channel channel = channelRepository.findById(dto.getChannelId()).orElse(null);

        if (sender == null || channel == null) {
            throw new IllegalArgumentException("Sender or Channel not found");
        }

        Message message = new Message(dto.getContent(), sender, channel);
        message.setTimestamp(LocalDateTime.now());

        // Si tu veux persister en base : messageRepository.save(message);
        return message;
    }
}
