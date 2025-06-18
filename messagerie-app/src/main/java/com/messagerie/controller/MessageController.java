package com.messagerie.controller;

import com.messagerie.dto.MessageDTO;
import com.messagerie.model.Channel;
import com.messagerie.model.Message;
import com.messagerie.model.User;
import com.messagerie.service.ChannelService;
import com.messagerie.service.MessageService;
import com.messagerie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, ChannelService channelService, UserService userService) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.userService = userService;
    }

    // POST - Envoyer un message via DTO
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO messageDTO) {
        Optional<User> sender = userService.findById(messageDTO.getSenderId());
        Optional<Channel> channel = Optional.ofNullable(channelService.getChannelById(messageDTO.getChannelId()));

        if (sender.isEmpty() || channel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSender(sender.get());
        message.setChannel(channel.get());
        message.setTimestamp(LocalDateTime.now());

        Message saved = messageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET - Récupérer tous les messages d'un channel
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable Long channelId) {
        Optional<Channel> channel = Optional.ofNullable(channelService.getChannelById(channelId));
        if (channel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Message> messages = messageService.getMessagesByChannel(channel.get());
        return ResponseEntity.ok(messages);
    }
}
