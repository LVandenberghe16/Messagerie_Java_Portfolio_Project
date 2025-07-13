package com.messagerie.controller;

import com.messagerie.dto.MessageDTO;
import com.messagerie.dto.MessageResponseDTO;
import com.messagerie.model.Channel;
import com.messagerie.model.Message;
import com.messagerie.model.User;
import com.messagerie.repository.ChannelRepository;
import com.messagerie.repository.MessageRepository;
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

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageResponseDTO handleMessage(MessageDTO dto) {
        User sender = userRepository.findById(dto.getSenderId())
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Channel channel = channelRepository.findById(dto.getChannelId())
            .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setSender(sender);
        message.setChannel(channel);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        return new MessageResponseDTO(
            message.getContent(),
            sender.getUsername(),
            channel.getId(),
            message.getTimestamp()
        );
    }
}