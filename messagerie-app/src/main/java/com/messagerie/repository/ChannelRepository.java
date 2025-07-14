package com.messagerie.repository;

import com.messagerie.model.Channel;
import com.messagerie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    /**
     * Recherche un canal privé partagé par u1 et u2
     */
    @Query("""
      SELECT c
        FROM Channel c
       WHERE c.isPrivate = true
         AND :u1 MEMBER OF c.members
         AND :u2 MEMBER OF c.members
    """)
    Optional<Channel> findPrivateChannel(
        @Param("u1") User u1,
        @Param("u2") User u2
    );
}
