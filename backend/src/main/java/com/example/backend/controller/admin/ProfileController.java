package com.example.backend.controller.admin;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public User getAdminProfile(Principal principal) {
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
}
