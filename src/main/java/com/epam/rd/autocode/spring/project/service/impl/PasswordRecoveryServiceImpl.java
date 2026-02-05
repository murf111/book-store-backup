package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.annotation.LogMask;
import com.epam.rd.autocode.spring.project.exception.InvalidTokenException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.User;
import com.epam.rd.autocode.spring.project.repository.UserRepository;
import com.epam.rd.autocode.spring.project.service.EmailService;
import com.epam.rd.autocode.spring.project.service.PasswordRecoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public void processForgotPassword(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String rawToken = UUID.randomUUID().toString();
            String hashedToken = passwordEncoder.encode(rawToken);

            user.setResetToken(hashedToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));

            userRepository.save(user);

            String link = baseUrl + "/password/reset?email=" + email + "&token=" + rawToken;

            emailService.sendPasswordRecovery(email, "Reset Password", "Click here to reset: " + link);
        });
    }

    @Override
    @Transactional
    public void resetPassword(String email,
                                 @LogMask String rawToken,
                                 @LogMask String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User"+email+"was not found"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token has expired. Please request a new one.");
        }

        if (!passwordEncoder.matches(rawToken, user.getResetToken())) {
            throw new InvalidTokenException("Invalid token.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}
