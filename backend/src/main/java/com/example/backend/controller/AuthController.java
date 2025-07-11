package com.example.backend.controller;

import com.example.backend.controller.dto.request.ForgotPasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.controller.dto.response.AuthResponseDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.model.entity.User;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public AuthResponseDTO registerUser(@ModelAttribute User user,
                                        @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return userService.registerUser(user, avatar);
    }

    @PostMapping("/signin")
    public AuthResponseDTO signin(@RequestBody SigninRequestDTO loginRequest) {
        AuthResponseDTO response = userService.login(loginRequest);
        if (response == null || response.getAccessToken() == null) {
            throw new BusinessException("Invalid email or password");
        }
        return response;
    }

    @PostMapping("/refresh-token")
    public AuthResponseDTO refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("Refresh token is missing");
        }

        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new BusinessException("Invalid token");
        }

        if (!jwtUtils.isRefreshToken(refreshToken)) {
            throw new BusinessException("This is not a refresh token");
        }

        String email = jwtUtils.getUserNameFromJwtToken(refreshToken);
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new BusinessException("User not found");
        }

        String newAccessToken = jwtUtils.generateAccessToken(email, user.getRole());

        return new AuthResponseDTO(newAccessToken, refreshToken, email, user.getName(), user.getRole(), user.getProfileImage());
    }

    @PostMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "Logout successful";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        String result = userService.forgotPasswordAndSendNewPassword(request.getEmail());
        if (result.startsWith("Error") || result.startsWith("Failed")) {
            throw new RuntimeException(result);
        }
        return result;
    }
}
