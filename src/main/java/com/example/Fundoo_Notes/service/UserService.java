package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.dto.LoginRequest;
import com.example.Fundoo_Notes.dto.UserRegisterRequest;
import com.example.Fundoo_Notes.dto.UserRegistrationEvent;
import com.example.Fundoo_Notes.entity.Role;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.messaging.RabbitMQProducer;
import com.example.Fundoo_Notes.repository.UserRepository;
import com.example.Fundoo_Notes.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RedisTokenService redisTokenService;

    // ✅ USER REGISTRATION
    public String registerUser(UserRegisterRequest dto) {

        // 1. Create User
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 2. Role Logic
        if (dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        // 3. Save to DB
        User savedUser = userRepository.save(user);

        // 4. Send RabbitMQ Event (ASYNC)
        rabbitMQProducer.sendUserRegistrationEvent(
                new UserRegistrationEvent(savedUser.getName(), savedUser.getEmail())
        );

        return "User Registered Successfully";
    }

    // ✅ LOGIN
    public String loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 1. Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // 2. Cache in Redis
        redisTokenService.cacheJwt(token, user.getEmail(), 3600);

        return token;
    }

    // ✅ FETCH USER (used in Notes/Profile)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}