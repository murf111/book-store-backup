package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.security.JwtUtil;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends BaseControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ClientService clientService;

    @Test
    void showRegistrationForm_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeExists("client"));
    }

    @Test
    void registerClient_ShouldRedirectToLogin_WhenSuccess() throws Exception {
        mockMvc.perform(post("/register")
                                .with(csrf())
                                .param("name", "John Doe")
                                .param("email", "john@example.com")
                                .param("password", "Password123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login?registerSuccess"));

        verify(clientService).addClient(any(ClientDTO.class));
    }

    @Test
    void registerClient_ShouldReturnForm_WhenValidationFails() throws Exception {
        // Missing name and password, invalid email
        mockMvc.perform(post("/register")
                                .with(csrf())
                                .param("email", "bad-email"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeHasFieldErrors("client", "name", "email", "password"));
    }

    @Test
    void registerClient_ShouldReturnForm_WhenEmailExists() throws Exception {
        when(clientService.addClient(any(ClientDTO.class)))
                .thenThrow(new AlreadyExistException("Email exists"));

        mockMvc.perform(post("/register")
                                .with(csrf())
                                .param("name", "John Doe")
                                .param("email", "existing@example.com")
                                .param("password", "Password123"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeHasFieldErrors("client", "email"));
    }
}