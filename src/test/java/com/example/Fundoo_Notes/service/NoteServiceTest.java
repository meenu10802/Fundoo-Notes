package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.dto.NoteRequest;
import com.example.Fundoo_Notes.entity.Note;
import com.example.Fundoo_Notes.entity.Role;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.messaging.rabbit.NoteEventProducer;
import com.example.Fundoo_Notes.repository.NoteRepository;
import com.example.Fundoo_Notes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteEventProducer noteEventProducer;

    @InjectMocks
    private NoteService noteService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);
    }

    @Test
    void createNoteShouldMapNoteToCurrentUser() {
        NoteRequest request = new NoteRequest();
        request.setTitle("Title");
        request.setContent("Body");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        String result = noteService.createNote("test@example.com", request);

        assertEquals("Note Created Successfully", result);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void getUserNotesShouldReturnOnlyCurrentUsersNotes() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(noteRepository.findByUserIdAndIsDeletedFalse(user.getId())).thenReturn(List.of(new Note()));

        List<Note> notes = noteService.getUserNotes("test@example.com");

        assertEquals(1, notes.size());
    }
}
