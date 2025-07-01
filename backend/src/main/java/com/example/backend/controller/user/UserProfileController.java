package com.example.backend.controller.user;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Principal principal) {
        User user = userService.getProfile(principal);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        User updated = userService.updateUserProfile(principal, user, img);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO request, Principal principal) {
        userService.changePassword(principal, request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
