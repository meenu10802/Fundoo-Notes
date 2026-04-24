package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.dto.LoginRequest;
import com.example.Fundoo_Notes.dto.UserRegisterRequest;
import com.example.Fundoo_Notes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRegisterRequest dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }
}