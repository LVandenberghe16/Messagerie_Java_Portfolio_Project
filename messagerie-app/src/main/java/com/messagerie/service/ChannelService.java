package com.messagerie.service;

import com.messagerie.model.Channel;
import com.messagerie.model.User;
import java.util.List;
import java.util.Optional;

public interface ChannelService {
    Channel createChannel(Channel channel);
    Channel getChannelById(Long id);
    List<Channel> getAllChannels();
    void deleteChannel(Long id);
    Optional<Channel> getPrivateChannelBetweenUsers(User user1, User user2);
}
