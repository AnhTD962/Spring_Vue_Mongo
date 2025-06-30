package com.example.backend.controller.admin;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        User user = userService.getUserByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        User dbUser = userService.getUserByEmail(principal.getName());
        if (dbUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Không update email vì email là cố định để định danh user
        user.setEmail(dbUser.getEmail());

        User updated = userService.updateUserProfile(dbUser, user, img);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Update failed");
        }

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO request, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        boolean matches = passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());
        if (!matches) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.updateUser(user);
        return ResponseEntity.ok("Password changed successfully");
    }
}
