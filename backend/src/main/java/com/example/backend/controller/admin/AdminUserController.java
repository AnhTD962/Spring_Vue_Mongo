package com.example.backend.controller.admin;

import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(value = "type", required = false) Integer type) {
        List<User> users;

        if (type == null) {
            users = userService.getAllUsers();
        } else if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserAccountStatus(@PathVariable String id, @RequestParam Boolean status) {
        boolean updated = userService.updateAccountStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Account Status Updated");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong on server");
        }
    }

    @PostMapping("/admins")
    public ResponseEntity<String> saveAdmin(@ModelAttribute User user,
                                            @RequestParam(value = "img", required = false) MultipartFile file) throws IOException {
        try {
            String imageName;
            if (file != null && !file.isEmpty()) {
                imageName = file.getOriginalFilename();
                user.setProfileImage(imageName);
                File uploadDir = new File("uploads/profile_img");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                Path path = Paths.get(uploadDir.getAbsolutePath(), imageName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } else {
                imageName = "default.jpg";
                user.setProfileImage(imageName);
            }

            User savedUser = userService.saveAdmin(user);
            return ResponseEntity.ok("Register successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Something went wrong on server");
        }
    }


}
