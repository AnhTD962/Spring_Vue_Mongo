package com.example.backend.controller.auth;

import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @ModelAttribute User user,
            @RequestParam(value = "avata", required = false) MultipartFile avata
    ) {
        if (userService.existsEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (avata != null && !avata.isEmpty()) {
            user.setProfileImage(avata.getOriginalFilename());
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequestDTO loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            User user = userService.getUserByEmail(loginRequest.getEmail());
            session.setAttribute("user", user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "user", user
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid credentials"));
        }
    }

    @PostMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "Logout successful";
    }
}