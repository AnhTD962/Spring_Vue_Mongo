package com.example.backend.service;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.controller.dto.response.AuthResponseDTO;
import com.example.backend.model.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {

    AuthResponseDTO refreshToken(String refreshToken);

    String signout(HttpSession session);

    User saveUser(User user);

    User getUserByEmail(String email);

    List<User> getUsers(String role);

    List<User> getAllUsers();

    User updateUserProfile(Principal principal, User update, MultipartFile img);

    User saveAdmin(User user);

    void uploadUserImage(MultipartFile file, String folder) throws Exception;

    User getProfile(Principal principal);

    String changePassword(Principal principal, ChangePasswordRequestDTO request);

    AuthResponseDTO  registerUser(User user, MultipartFile avatar);

    AuthResponseDTO login(SigninRequestDTO loginRequest);

    String forgotPasswordAndSendNewPassword(String email);

    List<User> getUsersByType(Integer type);

    User getUserOrThrow(String id);

    String updateAccountStatusOrThrow(String id, Boolean status);

    String registerAdmin(User user, MultipartFile file);
}
