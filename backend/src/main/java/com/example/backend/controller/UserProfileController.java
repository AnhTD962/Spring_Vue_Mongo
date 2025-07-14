package com.example.backend.controller;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/profile")
    public User getUserProfile(Principal principal) {
        return userService.getProfile(principal);
    }

    @PutMapping("/user/profile")
    public User updateUserProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        return userService.updateUserProfile(principal, user, img);
    }

    @PutMapping("/user/change-password")
    public String changePassword(@RequestBody ChangePasswordRequestDTO request, Principal principal) {
        userService.changePassword(principal, request);
        return "Password changed successfully";
    }

    @GetMapping("/admin/profile")
    public User getAdminProfile(Principal principal) {
        return userService.getProfile(principal);
    }

    @PutMapping("/admin/profile")
    public User updateAdminProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        return userService.updateUserProfile(principal, user, img);
    }

    @PutMapping("/admin/change-password")
    public String changeAdminPassword(@RequestBody ChangePasswordRequestDTO request, Principal principal) {
        userService.changePassword(principal, request);
        return "Password changed successfully";
    }
}
