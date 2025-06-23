package com.example.backend.controller.dto.response;

import com.example.backend.model.entity.User;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private User user;

    public LoginResponseDTO(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Getters và setters
}