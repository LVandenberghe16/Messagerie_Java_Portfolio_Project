package com.messagerie.service;

import com.messagerie.model.Channel;
import com.messagerie.model.Message;
import java.util.List;

public interface MessageService {
    Message sendMessage(Message message);
    List<Message> getMessagesByChannel(Channel channel);
}