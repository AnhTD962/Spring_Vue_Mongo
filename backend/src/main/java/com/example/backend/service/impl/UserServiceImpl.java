package com.example.backend.service.impl;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.controller.dto.response.AuthResponseDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.UserService;
import com.example.backend.util.AppConstant;
import com.example.backend.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AuthResponseDTO  registerUser(User user, MultipartFile avatar) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                File uploadDir = new File("uploads/profile_img");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String originalFilename = avatar.getOriginalFilename();
                assert originalFilename != null;
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID() + extension;

                Path path = Paths.get(uploadDir.getAbsolutePath(), uniqueFilename);
                Files.copy(avatar.getInputStream(), path);

                user.setProfileImage(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload avatar", e);
            }
        } else {
            user.setProfileImage("default.jpg");
        }

        saveUser(user);

        try {
            commonUtil.sendWelcomeEmail(user.getEmail(), user.getName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }

        String token = jwtUtils.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        return new AuthResponseDTO(token, refreshToken, user.getEmail(), user.getName(), user.getRole(), user.getProfileImage());
    }

    @Override
    public AuthResponseDTO login(SigninRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = getUserByEmail(loginRequest.getEmail());
            String jwt = jwtUtils.generateAccessToken(user.getEmail(), user.getRole());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

            return new AuthResponseDTO(jwt, refreshToken, user.getEmail(), user.getName(), user.getRole(), user.getProfileImage());
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public User saveAdmin(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User updateUserProfile(Principal principal, User update, MultipartFile img) {
        User dbUser = getProfile(principal);

        if (hasText(update.getName())) dbUser.setName(update.getName());
        if (hasText(update.getMobileNumber())) dbUser.setMobileNumber(update.getMobileNumber());
        if (hasText(update.getAddress())) dbUser.setAddress(update.getAddress());
        if (hasText(update.getCity())) dbUser.setCity(update.getCity());
        if (hasText(update.getState())) dbUser.setState(update.getState());
        if (hasText(update.getPincode())) dbUser.setPincode(update.getPincode());

        if (img != null && !img.isEmpty()) {
            try {
                File uploadDir = new File("uploads/profile_img");
                if (!uploadDir.exists()) uploadDir.mkdirs();
                String originalFilename = img.getOriginalFilename();
                assert originalFilename != null;
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID() + extension;

                Path path = Paths.get(uploadDir.getAbsolutePath(), uniqueFilename);
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                dbUser.setProfileImage(uniqueFilename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userRepository.save(dbUser);
    }

    @Override
    public User getProfile(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }
        return user;
    }

    @Override
    public String changePassword(Principal principal, ChangePasswordRequestDTO request) {
        User user = getProfile(principal);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

    @Override
    public void uploadUserImage(MultipartFile file, String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    @Override
    public String forgotPasswordAndSendNewPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "Error: Email not found";
        }
        String newPassword = generateRandomPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        try {
            commonUtil.sendNewPasswordEmail(user.getEmail(), newPassword);
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }

        return "New password sent to your email";
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }

        return sb.toString();
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("Refresh token is missing");
        }

        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new BusinessException("Invalid token");
        }

        if (!jwtUtils.isRefreshToken(refreshToken)) {
            throw new BusinessException("This is not a refresh token");
        }

        String email = jwtUtils.getUserNameFromJwtToken(refreshToken);
        User user = getUserByEmail(email);
        if (user == null) {
            throw new BusinessException("User not found");
        }

        String newAccessToken = jwtUtils.generateAccessToken(email, user.getRole());

        return new AuthResponseDTO(newAccessToken, refreshToken, email, user.getName(), user.getRole(), user.getProfileImage());
    }

    @Override
    public String signout(HttpSession session) {
        session.invalidate();
        return "Logout successful";
    }

    @Override
    public String registerAdmin(User user, MultipartFile file) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            user.setProfileImage(filename);
            try {
                uploadUserImage(file, "uploads/profile_img");
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile image", e);
            }
        } else {
            user.setProfileImage("default.jpg");
        }

        saveAdmin(user);
        return "Register successfully";
    }

    @Override
    public String updateAccountStatusOrThrow(String id, Boolean status) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = optionalUser.get();
        user.setIsEnable(status);
        userRepository.save(user);
        return "Account Status Updated";
    }

    @Override
    public User getUserOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public List<User> getUsersByType(Integer type) {
        if (type == null) return getAllUsers();
        return switch (type) {
            case 1 -> getUsers("ROLE_USER");
            case 2 -> getUsers("ROLE_ADMIN");
            default -> getAllUsers();
        };
    }

}
