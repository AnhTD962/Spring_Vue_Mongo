package com.example.backend.controller.auth;

import com.example.backend.controller.dto.request.ForgotPasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                                               @RequestParam(value = "avata", required = false) MultipartFile avata) {
        return userService.registerUser(user, avata);
    }

    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SigninRequestDTO loginRequest, HttpSession session) {
        Map<String, Object> response = userService.login(loginRequest, session);
        if (!(boolean) response.get("success")) {
            throw new BusinessException("Invalid email or password");
        }
        return response;
    }

    @PostMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "Logout successful";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        String result = userService.forgotPasswordAndSendNewPassword(request.getEmail());
        if (result.startsWith("Error") || result.startsWith("Failed")) {
            throw new RuntimeException(result);
        }
        return result;
    }
}
