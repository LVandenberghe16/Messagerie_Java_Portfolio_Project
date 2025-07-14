package com.messagerie.controller;

import com.messagerie.dto.ChannelDTO;
import com.messagerie.dto.MessageDTO;
import com.messagerie.model.Channel;
import com.messagerie.model.User;
import com.messagerie.service.ChannelService;
import com.messagerie.service.MessageService;
import com.messagerie.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService   channelService;
    private final UserService      userService;
    private final MessageService   messageService;

    public ChannelController(ChannelService channelService,
                             UserService userService,
                             MessageService messageService) {
        this.channelService = channelService;
        this.userService    = userService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getAllChannels() {
        List<ChannelDTO> dtos = channelService.getAllChannels().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDTO> getChannelById(@PathVariable Long id) {
        Optional<Channel> opt = channelService.getChannelById(id);
        return opt
            .map(c -> ResponseEntity.ok(convertToDTO(c)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ChannelDTO> createChannel(@RequestBody ChannelDTO dto) {
        Channel toSave = convertToEntity(dto);
        Channel saved  = channelService.createChannel(toSave);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(convertToDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/private/{username1}/{username2}")
    public ResponseEntity<ChannelDTO> getPrivateChannelBetweenUsers(
            @PathVariable String username1,
            @PathVariable String username2) {

        User u1 = userService.findUserEntityByUsername(username1)
                   .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur " + username1 + " non trouvé"));
        User u2 = userService.findUserEntityByUsername(username2)
                   .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur " + username2 + " non trouvé"));

        Channel channel = channelService.getPrivateChannel(u1, u2)
            .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aucun canal privé trouvé"));

        return ResponseEntity.ok(convertToDTO(channel));
    }

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<List<MessageDTO>> listMessages(@PathVariable Long channelId) {
        Channel channel = channelService.getChannelById(channelId)
            .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Canal " + channelId + " non trouvé"));

        List<MessageDTO> messages = messageService.getMessagesByChannel(channel).stream()
            .map(MessageDTO::fromEntity)
            .collect(Collectors.toList());

        return ResponseEntity.ok(messages);
    }

    // ----------------------------------------------------
    // Méthodes auxiliaires de conversion Entity ⇄ DTO
    // ----------------------------------------------------

    private ChannelDTO convertToDTO(Channel c) {
        List<Long> memberIds = c.getMembers().stream()
            .map(User::getId)
            .collect(Collectors.toList());
        return new ChannelDTO(
            c.getId(),
            c.getName(),
            c.getIsPrivate(),
            memberIds
        );
    }

    private Channel convertToEntity(ChannelDTO dto) {
        Channel c = new Channel();
        c.setName(dto.getName());
        c.setIsPrivate(dto.getIsPrivate());
        // Si besoin d'ajouter des membres dès la création, injectez-les ici
        return c;
    }
}
