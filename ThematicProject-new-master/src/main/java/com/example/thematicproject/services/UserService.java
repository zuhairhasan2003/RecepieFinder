package com.example.thematicproject.services;

import com.example.thematicproject.models.User;
import com.example.thematicproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register a new user
    public String registerUser(String username, String email, String password) {
        // Check if the user already exists
        if (userRepository.existsByUsername(username)) {
            return "Username already exists";
        }

        if (userRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        // Create a new User object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(password));

        // Save the user to the repository
        userRepository.save(user);

        return "User registered successfully";
    }

    public String loginUser(String username, String password) {
        // Find the user by username
        Optional<User> user = userRepository.findByUsername(username);

        // Check if the user exists
        if (user.isEmpty()) {
            return "User not found";
        }

        // Compare the provided password with the encrypted password
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return "Invalid password";
        }

        // Return success message (or JWT in a real application)
        return "Login successful";
    }


}
