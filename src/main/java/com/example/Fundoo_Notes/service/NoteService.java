package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.dto.NoteCreationEvent;
import com.example.Fundoo_Notes.dto.NoteRequest;
import com.example.Fundoo_Notes.entity.Note;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.messaging.RabbitMQProducer;
import com.example.Fundoo_Notes.repository.NoteRepository;
import com.example.Fundoo_Notes.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @CacheEvict(value = "userNotes", key = "#email")
    public String createNote(String email, NoteRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUserId(user.getId());

        Note savedNote = noteRepository.save(note);

        rabbitMQProducer.sendNoteCreationEvent(
                new NoteCreationEvent(savedNote.getUserId(), savedNote.getTitle())
        );

        return "Note Created Successfully";
    }

    @Cacheable(value = "userNotes", key = "#email")
    public List<Note> getUserNotes(String email) {

        System.out.println("Fetching notes from database for: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.findByUserIdAndIsDeletedFalse(user.getId());
    }

    @CacheEvict(value = "userNotes", key = "#email")
    public String updateNote(String email, Long noteId, NoteRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUserId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this note");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        noteRepository.save(note);

        return "Note Updated Successfully";
    }

    @CacheEvict(value = "userNotes", key = "#email")
    public String deleteNote(String email, Long noteId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUserId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this note");
        }

        note.setDeleted(true);
        noteRepository.save(note);

        return "Note Deleted Successfully";
    }

    @CacheEvict(value = "userNotes", key = "#email")
    public String togglePin(String email, Long noteId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUserId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this note");
        }

        note.setPinned(!note.isPinned());
        noteRepository.save(note);

        return "Pin status updated successfully";
    }

    @CacheEvict(value = "userNotes", key = "#email")
    public String toggleArchive(String email, Long noteId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUserId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this note");
        }

        note.setArchived(!note.isArchived());
        noteRepository.save(note);

        return "Archive status updated successfully";
    }
}