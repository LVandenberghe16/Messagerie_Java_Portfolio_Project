package com.messagerie.controller;

import com.messagerie.dto.LoginDTO;
import com.messagerie.dto.UserDTO;
import com.messagerie.dto.UserRegistrationDTO;
import com.messagerie.service.UserService;
import com.messagerie.model.User;
import com.messagerie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegistrationDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 🔒 Tu peux hasher ici si tu veux
        user = userRepository.save(user);
        System.out.println("register");
        return UserDTO.fromEntity(user); // ✅ renvoie un UserDTO sécurisé
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("logged in");
        return userService.authenticate(loginDTO.getEmail(), loginDTO.getPassword())
                .map(user -> ResponseEntity.ok(UserDTO.fromEntity(user)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    }
}
