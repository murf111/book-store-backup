package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.UserDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.PaymentService;
import com.epam.rd.autocode.spring.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;
    @MockBean private ClientService clientService;
    @MockBean private EmployeeService employeeService;
    @MockBean private PaymentService paymentService;

    @Test
    @WithMockUser(username = "client@test.com", roles = "CLIENT")
    void showProfile_ShouldReturnProfileView() throws Exception {
        UserDTO userDTO = UserDTO.builder().email("client@test.com").role("CLIENT").build();
        when(userService.getCurrentUser()).thenReturn(userDTO);
        when(clientService.getClientByEmail("client@test.com")).thenReturn(new ClientDTO());

        mockMvc.perform(get("/profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("profile"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "client@test.com")
    void topUp_ShouldProcessPaymentAndRedirect() throws Exception {
        mockMvc.perform(post("/profile/topup")
                                .with(csrf())
                                .param("amount", "100.00")
                                .param("cardNumber", "1234567812345678")
                                .param("expirationDate", "12/26")
                                .param("cvv", "123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/profile"));

        verify(paymentService).processPayment(eq("client@test.com"), any());
    }
}