package com.example.backend.controller.auth;

import com.example.backend.controller.dto.request.ForgotPasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> registerUser(@ModelAttribute User user,
                                               @RequestParam(value = "avata", required = false) MultipartFile avata) {
        return ResponseEntity.ok(userService.registerUser(user, avata));
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@RequestBody SigninRequestDTO loginRequest, HttpSession session) {
        Map<String, Object> response = userService.login(loginRequest, session);
        if (!(boolean) response.get("success")) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        String result = userService.forgotPasswordAndSendNewPassword(request.getEmail());
        if (result.startsWith("Error") || result.startsWith("Failed")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }
}
