package com.messagerie.service;

import com.messagerie.model.Message;
import com.messagerie.model.Channel;
import com.messagerie.repository.MessageRepository;
import com.messagerie.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return messageRepository.findByChannelOrderByTimestampAsc(channel);
    }
}
