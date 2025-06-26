package com.example.backend.service.impl;

import com.example.backend.model.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Boolean updateAccountStatus(String id, Boolean status) {
        Optional<User> findByuser = userRepository.findById(id);

        if (findByuser.isPresent()) {
            User User = findByuser.get();
            User.setIsEnable(status);
            userRepository.save(User);
            return true;
        }

        return false;
    }

    @Override
    public void increaseFailedAttempt(User user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
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
        long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unLockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void resetAttempt(int userId) {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        User findByEmail = userRepository.findByEmail(email);
        findByEmail.setResetToken(resetToken);
        userRepository.save(findByEmail);
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(User dbUser, User user, MultipartFile img) {
        if (dbUser == null) {
            throw new IllegalArgumentException("dbUser cannot be null");
        }

        if (hasText(user.getName())) {
            dbUser.setName(user.getName());
        }
        if (hasText(user.getMobileNumber())) {
            dbUser.setMobileNumber(user.getMobileNumber());
        }
        if (hasText(user.getAddress())) {
            dbUser.setAddress(user.getAddress());
        }
        if (hasText(user.getCity())) {
            dbUser.setCity(user.getCity());
        }
        if (hasText(user.getState())) {
            dbUser.setState(user.getState());
        }
        if (hasText(user.getPincode())) {
            dbUser.setPincode(user.getPincode());
        }

        // Handle profile image
        if (img != null && !img.isEmpty()) {
            try {
                File saveFile = new ClassPathResource("static/img").getFile();
                File uploadDir = new File(saveFile, "profile_img");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                Path path = Paths.get(uploadDir.getAbsolutePath(), img.getOriginalFilename());
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                dbUser.setProfileImage("profile_img/" + img.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userRepository.save(dbUser);
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }


    @Override
    public User saveAdmin(User user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public Boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
