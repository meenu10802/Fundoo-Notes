package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.dto.RegisterRequestDTO;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.exception.UserAlreadyExistsException;
import com.example.Fundoo_Notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(RegisterRequestDTO dto) {

        // Check if user already exists
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("Email already registered");
                });

        // Convert DTO → Entity
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // ⚠️ plain for now (hash in UC5)

        userRepository.save(user);

        return "User registered successfully";
    }
}