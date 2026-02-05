package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.conf.CustomAuthFailureHandler;
import com.epam.rd.autocode.spring.project.conf.SecurityConfig;
import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.service.CartService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class) // Import Security Config for Thymeleaf #authorization support
class CartControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private CartService cartService;
    @MockBean private OrderService orderService;

    // [FIX] Mock ALL beans required by SecurityConfig
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private CustomAuthFailureHandler failureHandler;

    @Test
    @WithMockUser(username = "user@test.com", roles = "CLIENT") // Populates security context
    void showCart_ShouldReturnCartView() throws Exception {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(new ArrayList<>());
        cartDTO.setTotalPrice(BigDecimal.ZERO);

        when(cartService.getCart(anyString())).thenReturn(cartDTO);

        mockMvc.perform(get("/cart"))
               .andExpect(status().isOk())
               .andExpect(view().name("cart"))
               .andExpect(model().attributeExists("items", "totalPrice"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "CLIENT")
    void addToCart_ShouldCallService_AndRedirect() throws Exception {
        mockMvc.perform(post("/cart/add/1")
                                .with(csrf()) // Required for POST/PUT/DELETE
                                .header("Referer", "/books"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/books"));

        verify(cartService).addBook("user@test.com", 1L);
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "CLIENT")
    void decreaseQuantity_ShouldCallService_AndRedirect() throws Exception {
        mockMvc.perform(post("/cart/decrease/1")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"));

        verify(cartService).decreaseBookQuantity("user@test.com", 1L);
    }
}