package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.dto.NoteRequest;
import com.example.Fundoo_Notes.entity.Note;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.repository.NoteRepository;
import com.example.Fundoo_Notes.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public String createNote(String email, NoteRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        // ✅ CORE CONCEPT: store only userId
        note.setUserId(user.getId());

        noteRepository.save(note);

        return "Note Created Successfully";
    }

    public List<Note> getUserNotes(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return noteRepository.findByUserIdAndIsDeletedFalse(user.getId());
    }
}