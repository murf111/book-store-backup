package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.User;
import com.epam.rd.autocode.spring.project.repository.UserRepository;
import com.epam.rd.autocode.spring.project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserServiceImpl}.
 * <p>
 * Verifies profile updates and secure password changes.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Test {@link UserServiceImpl#changePassword(String, String)}.
     * <p>
     * <strong>Scenario:</strong> User provides correct old password.
     * <strong>Check:</strong> Password is updated and encoded.
     * </p>
     */
    @Test
    void changePassword_ShouldSucceed_WhenOldPasswordIsCorrect() {
        // Mock Security Context to get current user
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@test.com");

        User user = new Client();
        user.setEmail("user@test.com");
        user.setPassword("encodedOldPass");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawOldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        boolean result = userService.changePassword("rawOldPass", "newPass");

        assertTrue(result);
        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository).save(user);
    }

    /**
     * Test {@link UserServiceImpl#changePassword(String, String)}.
     * <p>
     * <strong>Scenario:</strong> User provides wrong old password.
     * <strong>Check:</strong> Update is rejected, DB is NOT touched.
     * </p>
     */
    @Test
    void changePassword_ShouldFail_WhenOldPasswordIsWrong() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@test.com");

        User user = new Client();
        user.setPassword("encodedOldPass");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedOldPass")).thenReturn(false);

        boolean result = userService.changePassword("wrongPass", "newPass");

        assertFalse(result);
        verify(userRepository, never()).save(any()); // Vital security check
    }
}