package com.example.backend.service.impl;

import com.example.backend.model.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");

        User user = userRepository.findByEmail(email);

        String errorMessage = "Email or password is invalid";

        if (user != null) {
            if (!user.getIsEnable()) {
                errorMessage = "Your account is inactive";
            } else if (!user.getAccountNonLocked()) {
                if (userService.unlockAccountTimeExpired(user)) {
                    errorMessage = "Your account is unlocked. Please try to login again.";
                } else {
                    errorMessage = "Your account is locked. Please try again later.";
                }
            } else {
                if (user.getFailedAttempt() < AppConstant.ATTEMPT_TIME - 1) {
                    userService.increaseFailedAttempt(user);
                    errorMessage = "Invalid credentials. Attempt: " + (user.getFailedAttempt() + 1);
                } else {
                    userService.userAccountLock(user);
                    errorMessage = "Your account is locked due to 3 failed attempts.";
                }
            }
        }

        // Set response as JSON
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }


}
