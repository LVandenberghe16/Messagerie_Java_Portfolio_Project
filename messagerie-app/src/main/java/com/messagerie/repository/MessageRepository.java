package com.messagerie.repository;

import com.messagerie.model.Message;
import com.messagerie.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Récupérer tous les messages d'un channel spécifique, triés par timestamp
    List<Message> findByChannelOrderByTimestampAsc(Channel channel);
}
