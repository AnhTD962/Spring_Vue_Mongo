package com.example.backend.controller.admin;

import com.example.backend.exception.BusinessException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(@RequestParam(value = "type", required = false) Integer type) {
        return (type == null)
                ? userService.getAllUsers()
                : switch (type) {
            case 1 -> userService.getUsers("ROLE_USER");
            case 2 -> userService.getUsers("ROLE_ADMIN");
            default -> userService.getAllUsers();
        };
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        return user;
    }

    @PutMapping("/users/{id}/status")
    public String updateUserAccountStatus(@PathVariable String id,
                                          @RequestParam Boolean status) {
        boolean updated = userService.updateAccountStatus(id, status);
        if (!updated) {
            throw new BusinessException("Failed to update account status");
        }
        return "Account Status Updated";
    }

    @PostMapping("/admins")
    public String saveAdmin(@ModelAttribute User user,
                            @RequestParam(value = "img", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            user.setProfileImage(file.getOriginalFilename());
            userService.uploadUserImage(file, "uploads/profile_img");
        } else {
            user.setProfileImage("default.jpg");
        }

        userService.saveAdmin(user); // May throw IllegalArgumentException
        return "Register successfully";
    }
}
