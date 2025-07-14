package com.example.backend.controller;

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
        return userService.getUsersByType(type);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserOrThrow(id);
    }

    @PutMapping("/users/{id}/status")
    public String updateUserAccountStatus(@PathVariable String id,
                                          @RequestParam Boolean status) {
        return userService.updateAccountStatusOrThrow(id, status);
    }

    @PostMapping("/admins")
    public String saveAdmin(@ModelAttribute User user,
                            @RequestParam(value = "img", required = false) MultipartFile file) {
        return userService.registerAdmin(user, file);
    }
}
