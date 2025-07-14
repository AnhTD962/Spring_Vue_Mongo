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

    @PostMapping("/register")
    public AuthResponseDTO registerUser(@ModelAttribute User user,
                                        @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return userService.registerUser(user, avatar);
    }

    @PostMapping("/signin")
    public AuthResponseDTO signin(@RequestBody SigninRequestDTO loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/refresh-token")
    public AuthResponseDTO refreshToken(@RequestBody Map<String, String> request) {
        return userService.refreshToken(request.get("refreshToken"));
    }

    @PostMapping("/signout")
    public String signout(HttpSession session) {
        return userService.signout(session);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        return userService.forgotPasswordAndSendNewPassword(request.getEmail());
    }
}
