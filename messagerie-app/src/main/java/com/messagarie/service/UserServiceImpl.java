package com.messagerie.service.impl;

import com.messagerie.model.User;
import com.messagerie.repository.UserRepository;
import com.messagerie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User createUser(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already taken");
        }
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepo.existsById(id) == true)
            userRepo.deleteById(id);
        else
            throw new RuntimeException("No user exists with ID: " + id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
