package com.messagerie.dto;

import com.messagerie.model.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;

    public UserDTO() {}

    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // ✅ Conversion depuis l'entité
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    // ✅ Conversion vers l'entité
    public User toEntity() {
        User user = new User();
        user.setID(this.id); // Optionnel si pas utilisé côté création
        user.setUsername(this.username);
        user.setEmail(this.email);
        // ⚠️ Pas de mot de passe ici car il n'est pas présent dans le DTO
        return user;
    }
}
