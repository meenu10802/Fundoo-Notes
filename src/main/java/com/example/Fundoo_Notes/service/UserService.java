package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.dto.LoginRequest;
import com.example.Fundoo_Notes.dto.UserRegisterRequest;
import com.example.Fundoo_Notes.entity.Role;
import com.example.Fundoo_Notes.entity.User;
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
    private RedisTokenService redisTokenService;

    public String registerUser(UserRegisterRequest dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // ✅ ROLE LOGIC
        if (dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);

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

    // ✅ NEW METHOD (PROFILE)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}