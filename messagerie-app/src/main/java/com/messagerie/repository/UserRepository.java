package com.messagerie.repository;

import com.messagerie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findUserEntityById(Long id);
    Optional<User> findUserEntityByUsername(String username);
    Optional<User> findById(Long id);
}
