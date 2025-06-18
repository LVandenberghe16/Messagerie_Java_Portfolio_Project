package com.messagerie.service;

import com.messagerie.model.Channel;
import com.messagerie.model.User;
import com.messagerie.repository.ChannelRepository;
import com.messagerie.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepo;

    @Autowired
    public ChannelServiceImpl(ChannelRepository channelRepo) {
        this.channelRepo = channelRepo;
    }

    @Override
    public Channel createChannel(Channel channel) {
        return channelRepo.save(channel);
    }

    @Override
    public Channel getChannelById(Long id) {
        return channelRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found with id: " + id));
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepo.findAll();
    }

    @Override
    public void deleteChannel(Long id) {
        if (channelRepo.existsById(id)) {
            channelRepo.deleteById(id);
        } else {
            throw new RuntimeException("No channel exists with id: " + id);
        }
    }

    @Override
    public Optional<Channel> getPrivateChannelBetweenUsers(User user1, User user2) {
        return channelRepo.findAll().stream()
            .filter(Channel::getIsPrivate)
            .filter(channel -> channel.getMembers().contains(user1) && channel.getMembers().contains(user2))
            .findFirst();
    }
}
