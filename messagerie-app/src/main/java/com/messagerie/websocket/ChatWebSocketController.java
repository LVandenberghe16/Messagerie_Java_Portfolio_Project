package com.messagerie.websocket;

import com.messagerie.model.Message;
import com.messagerie.model.User;
import com.messagerie.model.Channel;
import com.messagerie.service.MessageService;
import com.messagerie.service.UserService;
import com.messagerie.service.ChannelService;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserService userService;
    private final ChannelService channelService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate,
                                   MessageService messageService,
                                   UserService userService,
                                   ChannelService channelService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userService = userService;
        this.channelService = channelService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(com.messagerie.dto.MessageDTO incoming) {
        Optional<User> senderOpt = userService.findUserEntityById(incoming.getSenderId());
        Optional<Channel> channelOpt = channelService.getChannelById(incoming.getChannelId());

        if (senderOpt.isEmpty() || channelOpt.isEmpty()) return;

        Message message = new Message();
        message.setContent(incoming.getContent());
        message.setSender(senderOpt.get());
        message.setChannel(channelOpt.get());
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageService.sendMessage(message);

        // Retourne un DTO vers le front ! (pas l'entité brute)
        messagingTemplate.convertAndSend(
            "/topic/channel/" + savedMessage.getChannel().getId(),
            com.messagerie.dto.MessageDTO.fromEntity(savedMessage)
        );
    }

}
