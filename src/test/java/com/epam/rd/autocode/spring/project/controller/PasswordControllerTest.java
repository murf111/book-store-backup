package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.security.JwtUtil;
import com.epam.rd.autocode.spring.project.service.PasswordRecoveryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PasswordController.class)
@AutoConfigureMockMvc(addFilters = false)
class PasswordControllerTest extends BaseControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PasswordRecoveryService recoveryService;

    @Test
    void forgotPassword_ShouldReturnView() throws Exception {
        mockMvc.perform(get("/password/forgot"))
               .andExpect(status().isOk())
               .andExpect(view().name("password/forgot"));
    }

    @Test
    void processForgotPassword_ShouldCallService_AndRedirect() throws Exception {
        mockMvc.perform(post("/password/forgot")
                                .with(csrf())
                                .param("email", "user@example.com"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/password/forgot"))
               .andExpect(view().name("redirect:/password/forgot"))
               .andExpect(flash().attributeExists("message"));

        verify(recoveryService).processForgotPassword("user@example.com");
    }

    @Test
    void processResetPassword_ShouldCallService_AndRedirect() throws Exception {
        // Fix: POST request must include 'email', 'token', and 'password'
        // to match the service signature resetPassword(email, token, password)
        mockMvc.perform(post("/password/reset")
                                .with(csrf())
                                .param("email", "user@example.com")
                                .param("token", "valid-token")
                                .param("newPassword", "NewPass123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login?resetSuccess"));

        // Fix: Verify calls the 3-argument method
        verify(recoveryService).resetPassword("user@example.com", "valid-token", "NewPass123");
    }
}