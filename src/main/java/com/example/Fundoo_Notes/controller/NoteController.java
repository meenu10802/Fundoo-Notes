package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.dto.NoteRequest;
import com.example.Fundoo_Notes.entity.Note;
import com.example.Fundoo_Notes.service.NoteService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<Note> getNotes(Authentication authentication) {

        String email = authentication.getName();
        return noteService.getUserNotes(email);
    }

    @PutMapping("/{noteId}")
    public String updateNote(@PathVariable Long noteId,
                             @Valid @RequestBody NoteRequest request,
                             Authentication authentication) {

        String email = authentication.getName();
        return noteService.updateNote(email, noteId, request);
    }

    @DeleteMapping("/{noteId}")
    public String deleteNote(@PathVariable Long noteId,
                             Authentication authentication) {

        String email = authentication.getName();
        return noteService.deleteNote(email, noteId);
    }

    @PatchMapping("/{noteId}/pin")
    public String togglePin(@PathVariable Long noteId,
                            Authentication authentication) {

        String email = authentication.getName();
        return noteService.togglePin(email, noteId);
    }

    @PatchMapping("/{noteId}/archive")
    public String toggleArchive(@PathVariable Long noteId,
                                Authentication authentication) {

        String email = authentication.getName();
        return noteService.toggleArchive(email, noteId);
    }
}