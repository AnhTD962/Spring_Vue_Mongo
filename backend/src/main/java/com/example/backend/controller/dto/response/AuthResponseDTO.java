package com.example.backend.controller.dto.response;

import lombok.Data;

@Data
public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken;
    private String email;
    private String fullName;
    private String role;
    private String profileImage;

    public AuthResponseDTO(String accessToken, String refreshToken, String email, String fullName, String role, String profileImage) {
        this.accessToken = accessToken;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.profileImage = profileImage;
    }

}
