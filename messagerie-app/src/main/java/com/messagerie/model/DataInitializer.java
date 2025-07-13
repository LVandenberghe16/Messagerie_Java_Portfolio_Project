package com.messagerie.model;

import com.messagerie.service.ChannelService;
import com.messagerie.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        // Vérifie si le canal "général" existe déjà
        boolean exists = channelService.getAllChannels().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase("général"));

        if (!exists) {
            Channel general = new Channel();
            general.setName("général");
            general.setIsPrivate(false);

            // Optionnel : ajouter un utilisateur par défaut au canal
            Optional<User> maybeUser = userService.findUserEntityById(1L);
            Set<User> members = new HashSet<>();
            maybeUser.ifPresent(members::add);
            general.setMembers(members);

            channelService.createChannel(general);
            System.out.println("✅ Canal 'général' créé avec succès.");
        } else {
            System.out.println("ℹ️ Canal 'général' déjà existant.");
        }
    }
}
