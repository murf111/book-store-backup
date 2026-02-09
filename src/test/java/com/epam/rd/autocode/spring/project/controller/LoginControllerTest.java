package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.security.JwtUtil;
import com.epam.rd.autocode.spring.project.service.LoginAttemptService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypass Security Filter chain for Unit Logic testing
class LoginControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private LoginAttemptService loginAttemptService;

    @Test
    void login_ShouldSetHttpOnlyCookie_WhenCredentialsAreValid() throws Exception {
        // Arrange
        String username = "user@example.com";
        String password = "password";
        String fakeToken = "eyJ.fake.token";

        // Mock Auth
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = new User(username, password, new ArrayList<>());
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        // Mock JWT
        when(jwtUtil.generateToken(userDetails)).thenReturn(fakeToken);

        // Act & Assert
        mockMvc.perform(post("/login")
                                .param("username", username)
                                .param("password", password))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/"))
               .andExpect(cookie().exists("accessToken"))
               .andExpect(cookie().value("accessToken", fakeToken))
               .andExpect(cookie().httpOnly("accessToken", true));
    }

    @Test
    void login_ShouldReturnLoginPage_WhenAuthFails() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad creds"));

        // Act & Assert
        // FIX: Expect 200 OK + View "login", NOT Redirect
        mockMvc.perform(post("/login")
                                .param("username", "wrong")
                                .param("password", "wrong"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"))
               .andExpect(model().attributeExists("error"));
    }
}