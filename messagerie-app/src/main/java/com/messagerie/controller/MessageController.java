package com.messagerie.controller;

import com.messagerie.dto.MessageDTO;
import com.messagerie.dto.MessageResponseDTO;
import com.messagerie.model.Channel;
import com.messagerie.model.Message;
import com.messagerie.service.ChannelService;
import com.messagerie.service.MessageService;
import com.messagerie.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;
    private final UserService userService;

    public MessageController(MessageService messageService,
                           ChannelService channelService,
                           UserService userService) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        var sender = userService.findById(messageDTO.getSenderId());
        var channel = channelService.getChannelById(messageDTO.getChannelId());

        if (sender.isEmpty() || channel.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSender(sender.get());
        message.setChannel(channel.get());

        Message saved = messageService.sendMessage(message);

        return ResponseEntity.ok(new MessageResponseDTO(
            saved.getContent(),
            saved.getSender().getUsername(),
            saved.getChannel().getId(),
            saved.getTimestamp()
        ));
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByChannel(@PathVariable Long channelId) {
        Optional<Channel> channel = channelService.getChannelById(channelId);
        if (channel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Message> messages = messageService.getMessagesByChannel(channel.get());

        return ResponseEntity.ok(messages.stream()
            .map(m -> new MessageResponseDTO(
                m.getContent(),
                m.getSender().getUsername(),
                m.getChannel().getId(),
                m.getTimestamp()
            ))
            .collect(Collectors.toList()));
    }
}