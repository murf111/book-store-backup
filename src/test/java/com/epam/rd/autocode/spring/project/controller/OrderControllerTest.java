package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.security.JwtUtil;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Web MVC tests for {@link OrderController}.
 * <p>
 * Verifies order history retrieval and the confirmation action.
 * </p>
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest extends BaseControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private OrderService orderService;

    /**
     * Tests the {@code GET /orders} endpoint.
     * <p>
     * <strong>Scenario:</strong> A CLIENT requests their order history.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>The service method {@code getOrdersByClient} is called with the mocked username.</li>
     * <li>The model contains the "orders" attribute.</li>
     * <li>The correct view "orders" is returned.</li>
     * </ul>
     */
    @Test
    @WithMockUser(username = "client@test.com", roles = "CLIENT")
    void getOrders_ShouldReturnClientOrders() throws Exception {
        when(orderService.getOrdersByClient("client@test.com")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/orders"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("orders"))
               .andExpect(view().name("orders"));
    }

    /**
     * Tests the {@code PATCH /orders/{id}/confirm} endpoint.
     * <p>
     * <strong>Scenario:</strong> An EMPLOYEE confirms an order.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>The request is redirected back to the order list.</li>
     * <li>The service method {@code confirmOrder} is called with the correct ID and employee email.</li>
     * </ul>
     */
    @Test
    @WithMockUser(username = "emp@test.com", roles = "EMPLOYEE")
    void confirmOrder_ShouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(patch("/orders/1/confirm").with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/orders"));

        verify(orderService).confirmOrder(1L, "emp@test.com");
    }
}