package com.example.backend.controller.dto.request;

import lombok.Data;

@Data
public class SigninRequestDTO {
    private String email;
    private String password;
}
