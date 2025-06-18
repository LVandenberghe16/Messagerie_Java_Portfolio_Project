package com.messagerie.controller;

import com.messagerie.dto.ChannelDTO;
import com.messagerie.model.Channel;
import com.messagerie.model.User;
import com.messagerie.service.ChannelService;
import com.messagerie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final UserService userService;

    @Autowired
    public ChannelController(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
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
        Optional<Channel> channelOpt = channelService.getChannelById(id);
        if (channelOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(convertToDTO(channelOpt.get()));
    }

    @PostMapping
    public ResponseEntity<ChannelDTO> createChannel(@RequestBody ChannelDTO channelDTO) {
        Channel channel = convertToEntity(channelDTO);
        Channel saved = channelService.createChannel(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
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

        Optional<User> user1 = userService.findUserEntityByUsername(username1);
        Optional<User> user2 = userService.findUserEntityByUsername(username2);

        if (user1.isEmpty() || user2.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Channel> channel = channelService.getPrivateChannelBetweenUsers(user1.get(), user2.get());
        return channel.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DTO → Entity
    private Channel convertToEntity(ChannelDTO dto) {
        Channel channel = new Channel();
        channel.setName(dto.getName());
        channel.setIsPrivate(dto.getIsPrivate());

        if (dto.getMemberIds() != null) {
            Set<User> members = dto.getMemberIds().stream()
                    .map(userService::findUserEntityById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            channel.setMembers(members);
        }

        return channel;
    }

    // Entity → DTO
    private ChannelDTO convertToDTO(Channel channel) {
        List<Long> memberIds = channel.getMembers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return new ChannelDTO(channel.getId(), channel.getName(), channel.getIsPrivate(), memberIds);
    }
}
