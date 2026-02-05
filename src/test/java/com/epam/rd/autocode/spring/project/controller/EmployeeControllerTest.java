package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.conf.CustomAuthFailureHandler;
import com.epam.rd.autocode.spring.project.conf.SecurityConfig;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class EmployeeControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private EmployeeService employeeService;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private CustomAuthFailureHandler failureHandler;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void showStaff_ShouldBeAccessibleForEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/staff"))
               .andExpect(status().isOk())
               .andExpect(view().name("staff"))
               .andExpect(model().attributeExists("employees"));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void showStaff_ShouldBeForbiddenForClients() throws Exception {
        mockMvc.perform(get("/staff"))
               .andExpect(status().isForbidden()); // 403 Access Denied
    }
}