package com.example.backend.controller.user;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public User getUserProfile(Principal principal) {
        return userService.getProfile(principal);
    }

    @PutMapping("/profile")
    public User updateProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        return userService.updateUserProfile(principal, user, img);
    }

    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequestDTO request, Principal principal) {
        userService.changePassword(principal, request);
        return "Password changed successfully";
    }
}
