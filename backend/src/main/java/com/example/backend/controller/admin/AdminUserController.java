package com.example.backend.controller.admin;

import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(value = "type", required = false) Integer type) {
        List<User> users = (type == null) ? userService.getAllUsers() :
                switch (type) {
                    case 1 -> userService.getUsers("ROLE_USER");
                    case 2 -> userService.getUsers("ROLE_ADMIN");
                    default -> userService.getAllUsers();
                };
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserAccountStatus(@PathVariable String id,
                                                          @RequestParam Boolean status) {
        boolean updated = userService.updateAccountStatus(id, status);
        return updated
                ? ResponseEntity.ok("Account Status Updated")
                : ResponseEntity.internalServerError().body("Failed to update account status");
    }

    @PostMapping("/admins")
    public ResponseEntity<String> saveAdmin(@ModelAttribute User user,
                                            @RequestParam(value = "img", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                user.setProfileImage(file.getOriginalFilename());
                userService.uploadUserImage(file, "uploads/profile_img");
            } else {
                user.setProfileImage("default.jpg");
            }

            userService.saveAdmin(user);
            return ResponseEntity.ok("Register successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong on server");
        }
    }
}
