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

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JmsProducer jmsProducer;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RedisTokenService redisTokenService;

    public String registerUser(UserRegisterRequest dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);

        rabbitMQProducer.sendUserRegistrationEvent(
                new UserRegistrationEvent(savedUser.getName(), savedUser.getEmail())
        );
        jmsProducer.sendMessage("User Registered: " + savedUser.getEmail());
        return "User Registered Successfully";
    }

    public String loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        redisTokenService.cacheJwt(token, user.getEmail(), 3600);

        return token;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String sendOtp(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        redisTokenService.cacheOtp(email, otp, 300);

        System.out.println("OTP for " + email + " is: " + otp);

        return "OTP sent successfully";
    }

    public String verifyOtp(String email, String otp) {

        boolean isValid = redisTokenService.verifyOtp(email, otp);

        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        return "OTP verified successfully";
    }

    public String logout(String token) {
        redisTokenService.deleteJwt(token);
        return "Logged out successfully";
    }
}