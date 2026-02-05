package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.User;
import com.epam.rd.autocode.spring.project.repository.UserRepository;
import com.epam.rd.autocode.spring.project.service.impl.LoginAttemptServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceImplTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private LoginAttemptServiceImpl loginService;

    @Test
    void loginFailed_ShouldLockUser_AfterMaxAttempts() {
        User user = new Client();
        user.setFailedAttempts(2); // One more fail to lock
        user.setEmail("user@test.com");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        loginService.loginFailed("user@test.com");

        assertEquals(3, user.getFailedAttempts());
        assertNotNull(user.getLockTime()); // Must be locked now
        verify(userRepository).save(user);
    }

    @Test
    void loginSucceeded_ShouldResetCounter() {
        User user = new Client();
        user.setFailedAttempts(2);
        user.setEmail("user@test.com");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        loginService.loginSucceeded("user@test.com");

        assertEquals(0, user.getFailedAttempts());
        assertNull(user.getLockTime());
    }
}