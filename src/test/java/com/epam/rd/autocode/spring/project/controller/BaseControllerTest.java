package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.security.SecurityConfig;
import com.epam.rd.autocode.spring.project.security.JwtAuthenticationFilter;
import com.epam.rd.autocode.spring.project.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected UserDetailsService userDetailsService;
}