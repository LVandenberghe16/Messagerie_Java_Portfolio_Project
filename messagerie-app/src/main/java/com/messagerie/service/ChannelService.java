package com.messagerie.service;

import com.messagerie.model.Channel;
import com.messagerie.model.User;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    List<Channel> getAllChannels();
    Optional<Channel> getChannelById(Long id);
    Channel createChannel(Channel c);
    void deleteChannel(Long id);

    //    Avant : Optional<Channel> getPrivateChannelBetweenUsers(User u1, User u2);
    //    Après :
    Optional<Channel> getPrivateChannel(User u1, User u2);
}
