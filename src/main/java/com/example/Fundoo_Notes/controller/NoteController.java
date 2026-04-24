package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.dto.NoteRequest;
import com.example.Fundoo_Notes.service.NoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public String createNote(@Valid @RequestBody NoteRequest request,
                             Authentication authentication) {

        String email = authentication.getName();

        return noteService.createNote(email, request);
    }
}