package com.epam.rd.autocode.spring.project.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    // A valid 256-bit Hex key for testing
    private final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long TEST_EXPIRATION = 1000 * 60 * 60; // 1 Hour

    @BeforeEach
    void setUp() {
        // Manually inject the @Value properties
        ReflectionTestUtils.setField(jwtUtil, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", TEST_EXPIRATION);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        UserDetails user = new User("testuser", "password", new ArrayList<>());

        String token = jwtUtil.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnCorrectSubject() {
        UserDetails user = new User("test@example.com", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(user);

        String username = jwtUtil.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_ForValidToken() {
        UserDetails user = new User("validUser", "pass", new ArrayList<>());
        String token = jwtUtil.generateToken(user);

        assertTrue(jwtUtil.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_ShouldReturnFalse_ForDifferentUser() {
        UserDetails user1 = new User("user1", "pass", new ArrayList<>());
        UserDetails user2 = new User("user2", "pass", new ArrayList<>());

        String token = jwtUtil.generateToken(user1);

        assertFalse(jwtUtil.isTokenValid(token, user2));
    }
}