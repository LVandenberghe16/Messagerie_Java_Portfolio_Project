package com.messagerie.service;

import com.messagerie.model.Channel;
import com.messagerie.model.User;
import com.messagerie.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService {

    // <-- Déclaration du repository
    private final ChannelRepository channelRepository;

    // <-- Injection via constructeur
    public ChannelServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Optional<Channel> getChannelById(Long id) {
        return channelRepository.findById(id);
    }

    @Override
    public Channel createChannel(Channel c) {
        return channelRepository.save(c);
    }

    @Override
    public void deleteChannel(Long id) {
        channelRepository.deleteById(id);
    }

    // <-- Votre méthode manquante
    @Override
    public Optional<Channel> getPrivateChannel(User u1, User u2) {
        return channelRepository.findPrivateChannel(u1, u2);
    }
}
