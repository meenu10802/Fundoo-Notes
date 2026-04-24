package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.dto.LoginRequest;
import com.example.Fundoo_Notes.dto.UserRegisterRequest;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.service.UserService;
import com.example.Fundoo_Notes.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRegisterRequest dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        return userService.sendOtp(email);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {
        return userService.verifyOtp(email, otp);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return userService.logout(token);
    }
}