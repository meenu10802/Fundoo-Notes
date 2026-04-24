package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.dto.RegisterRequestDTO;
import com.example.Fundoo_Notes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO dto) {
        return userService.registerUser(dto);
    }
}