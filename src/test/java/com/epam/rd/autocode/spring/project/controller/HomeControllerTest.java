package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.conf.CustomAuthFailureHandler;
import com.epam.rd.autocode.spring.project.security.SecurityConfig;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest extends BaseControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private BookService bookService;
    @MockBean private CustomAuthFailureHandler failureHandler;

    @Test
    void home_ShouldReturnHomeView() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"))
               .andExpect(model().attributeExists("books"));
    }
}