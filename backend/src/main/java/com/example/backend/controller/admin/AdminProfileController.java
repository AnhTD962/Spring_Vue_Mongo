package com.example.backend.controller.admin;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getAdminProfile(Principal principal) {
        User user = userService.getProfile(principal);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        User updatedUser = userService.updateUserProfile(principal, user, img);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            Principal principal
    ) {
        String message = userService.changePassword(principal, request);
        return ResponseEntity.ok(message);
    }
}
