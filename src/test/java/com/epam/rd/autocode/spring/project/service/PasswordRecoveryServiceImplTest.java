package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.exception.InvalidTokenException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.User;
import com.epam.rd.autocode.spring.project.repository.UserRepository;
import com.epam.rd.autocode.spring.project.service.impl.PasswordRecoveryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private PasswordRecoveryServiceImpl recoveryService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(recoveryService, "baseUrl", "http://localhost:8080");
    }

    @Test
    void processForgotPassword_ShouldGenerateToken_AndSendEmail() {
        // Arrange
        String email = "client@example.com";
        User user = new Client();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // [FIX] Stub the encoder so it returns a value instead of null
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-token-value");

        // Act
        recoveryService.processForgotPassword(email);

        // Assert
        assertNotNull(user.getResetToken()); // This will now pass
        assertNotNull(user.getResetTokenExpiry());
        verify(userRepository).save(user);

        verify(emailService).sendPasswordRecovery(
                eq(email),
                eq("Reset Password"),
                contains("http://localhost:8080/password/reset?email=" + email + "&token=")
        );
    }

    @Test
    void resetPassword_ShouldUpdatePassword_WhenTokenIsValid() {
        // Arrange
        String email = "client@example.com";
        String token = "valid-token";
        String newPass = "NewPass123";

        User user = new Client();
        user.setEmail(email);
        user.setResetToken("hashed-token-in-db"); // Simulate what's in DB
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPass)).thenReturn("encodedPass");

        // FIX: Explicitly mock the matches check!
        when(passwordEncoder.matches(eq(token), anyString())).thenReturn(true);

        // Act
        recoveryService.resetPassword(email, token, newPass);

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_ShouldThrow_WhenTokenExpired() {
        // Arrange
        String email = "client@example.com";
        String token = "expired-token";

        User user = new Client();
        user.setEmail(email);
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().minusHours(1)); // Expired

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(InvalidTokenException.class,
                     () -> recoveryService.resetPassword(email, token, "pass"));
    }

    @Test
    void resetPassword_ShouldThrow_WhenTokenDoesNotMatch() {
        // Arrange
        String email = "client@example.com";
        String inputToken = "wrong-token";
        String storedToken = "actual-token";

        User user = new Client();
        user.setEmail(email);
        user.setResetToken(storedToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(InvalidTokenException.class,
                     () -> recoveryService.resetPassword(email, inputToken, "pass"));
    }
}