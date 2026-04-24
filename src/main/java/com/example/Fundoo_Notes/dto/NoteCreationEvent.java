package com.example.Fundoo_Notes.dto;

import java.io.Serializable;

public class NoteCreationEvent implements Serializable {

    private Long userId;
    private String title;

    public NoteCreationEvent() {
    }

    public NoteCreationEvent(Long userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }
}