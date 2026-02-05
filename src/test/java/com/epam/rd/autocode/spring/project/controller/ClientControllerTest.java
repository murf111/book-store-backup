package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ClientService clientService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void showClients_ShouldReturnList() throws Exception {
        when(clientService.getAllClients()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/clients"))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andExpect(model().attributeExists("clients"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void blockClient_ShouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(patch("/clients/user@test.com/block")
                                .with(csrf())
                                .param("block", "true"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/clients"));

        verify(clientService).blockClient("user@test.com", true);
    }
}