package com.messagerie.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.messagerie.model.Message;
import com.messagerie.model.Channel;

import java.util.List;

public interface MessageService {
    Message sendMessage(Message message);
    List<Message> getMessagesByChannel(Channel channel);
}
