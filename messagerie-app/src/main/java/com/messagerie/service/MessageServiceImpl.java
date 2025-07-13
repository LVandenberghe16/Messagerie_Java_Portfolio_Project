package com.messagerie.service;

import com.messagerie.model.Channel;
import com.messagerie.model.Message;
import com.messagerie.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message sendMessage(Message message) {
        message.setTimestamp(java.time.LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return messageRepository.findByChannelOrderByTimestampAsc(channel);
    }
}