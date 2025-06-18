package com.messagerie.service;

import com.messagerie.dto.UserDTO;
import java.util.Optional;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    void deleteUser(Long id);
    Optional<UserDTO> findByUsername(String username);
    Optional<UserDTO> findByEmail(String email);
}