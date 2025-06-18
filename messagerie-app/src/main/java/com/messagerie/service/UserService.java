package com.messagerie.service;

import com.messagerie.dto.UserDTO;
import com.messagerie.model.User;


import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO);
    void deleteUser(Long id);
    Optional<UserDTO> findByUsername(String username);
    Optional<UserDTO> findByEmail(String email);
    Optional<User> findUserEntityById(Long id);
    Optional<User> findUserEntityByUsername(String username);
    Optional<User> findById(Long id);
}
