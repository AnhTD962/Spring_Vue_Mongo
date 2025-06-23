package com.example.backend.controller;

import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.controller.dto.response.LoginResponseDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Đăng ký
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userService.existsEmail(user.getEmail())) {
            return "Email already exists";
        }

        userService.saveUser(user);
        return "User registered successfully";
    }

    // Đăng nhập
    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> signin(@RequestBody SigninRequestDTO loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.getUserByEmail(loginRequest.getEmail());
            session.setAttribute("user", user);

            return ResponseEntity.ok(new LoginResponseDTO(true, "Login successful", user));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Invalid credentials", null));
        }
    }

    // Đăng xuất
    @PostMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "Logout successful";
    }
}