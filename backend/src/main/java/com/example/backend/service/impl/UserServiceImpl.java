package com.example.backend.service.impl;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.model.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.util.AppConstant;
import com.example.backend.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
    public String registerUser(User user, MultipartFile avatar) {
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
            return "User registered, but failed to send confirmation email: " + e.getMessage();
        }

        return "User registered successfully. Confirmation email sent.";
    }


    @Override
    public Map<String, Object> login(SigninRequestDTO loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            User user = getUserByEmail(loginRequest.getEmail());
            session.setAttribute("user", user);

            return Map.of(
                    "success", true,
                    "message", "Login successful",
                    "user", user
            );
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "message", "Invalid credentials"
            );
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
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
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
    public Boolean updateAccountStatus(String id, Boolean status) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsEnable(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(User user) {
        user.setFailedAttempt(user.getFailedAttempt() + 1);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(User user) {
        long lockTime = user.getLockTime().getTime();
        long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
        if (System.currentTimeMillis() > unlockTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        User user = userRepository.findByEmail(email);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
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


}
