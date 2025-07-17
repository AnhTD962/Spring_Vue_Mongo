package com.example.backend.controller;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<User> getAllUsers(@RequestParam(value = "type", required = false) Integer type) {
        return userService.getUsersByType(type);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserOrThrow(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/status")
    public String updateUserAccountStatus(@PathVariable String id,
                                          @RequestParam Boolean status) {
        return userService.updateAccountStatusOrThrow(id, status);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-admin")
    public String saveAdmin(@ModelAttribute User user,
                            @RequestParam(value = "img", required = false) MultipartFile file) {
        return userService.registerAdmin(user, file);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/profile")
    public User getProfile(Principal principal) {
        return userService.getProfile(principal);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/profile")
    public User updateProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        return userService.updateUserProfile(principal, user, img);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequestDTO request, Principal principal) {
        return userService.changePassword(principal, request);
    }
}
