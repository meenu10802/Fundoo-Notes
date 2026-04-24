package com.example.Fundoo_Notes.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/test")
    public String adminAccess() {
        return "Admin API Accessed!";
    }
}
