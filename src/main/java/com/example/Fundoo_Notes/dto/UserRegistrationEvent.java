package com.example.Fundoo_Notes.dto;

import java.io.Serializable;

public class UserRegistrationEvent implements Serializable {

    private String name;
    private String email;

    public UserRegistrationEvent() {
    }

    public UserRegistrationEvent(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}