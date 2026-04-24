package com.personalproject.tasklist.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    
    @PostMapping("/")
    public ResponseEntity create(@RequestBody User user) {
        var collectedUser = this.userRepository.findByUsername(user.getUsername());
        if (collectedUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esse username já está em uso");
        }
        
        var encryptedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(encryptedPassword);

        var createdUser = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
