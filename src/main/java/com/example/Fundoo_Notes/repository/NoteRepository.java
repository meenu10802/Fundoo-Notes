package com.example.Fundoo_Notes.repository;

import com.example.Fundoo_Notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserIdAndIsDeletedFalse(Long userId);

    List<Note> findByUserIdAndIsArchivedFalseAndIsDeletedFalse(Long userId);
}