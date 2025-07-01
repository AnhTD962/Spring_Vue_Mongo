package com.example.backend.service;

import com.example.backend.controller.dto.request.ChangePasswordRequestDTO;
import com.example.backend.controller.dto.request.SigninRequestDTO;
import com.example.backend.model.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {

    User saveUser(User user);

    User getUserByEmail(String email);

    List<User> getUsers(String role);

    List<User> getAllUsers();

    Boolean updateAccountStatus(String id, Boolean status);

    void increaseFailedAttempt(User user);

    void userAccountLock(User user);

    boolean unlockAccountTimeExpired(User user);

    void updateUserResetToken(String email, String resetToken);

    void updateUser(User user);

    User updateUserProfile(Principal principal, User update, MultipartFile img);

    User saveAdmin(User user);

    User getUserById(String id);

    void uploadUserImage(MultipartFile file, String folder) throws Exception;

    User getProfile(Principal principal);


    String changePassword(Principal principal, ChangePasswordRequestDTO request);

    String registerUser(User user, MultipartFile avatar);

    Map<String, Object> login(SigninRequestDTO loginRequest, HttpSession session);

    String forgotPasswordAndSendNewPassword(String email);
}
