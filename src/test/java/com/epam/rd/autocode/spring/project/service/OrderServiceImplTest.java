package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.model.enums.Role;
import com.epam.rd.autocode.spring.project.repository.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repository.OrderRepository;
import com.epam.rd.autocode.spring.project.service.impl.EmailServiceImpl;
import com.epam.rd.autocode.spring.project.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link OrderServiceImpl}.
 * <p>
 * Tests the business logic for managing orders, including status updates and confirmations.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmailServiceImpl emailService;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    /**
     * Tests the {@link OrderServiceImpl#confirmOrder(Long, String)} method.
     * <p>
     * <strong>Scenario:</strong> An employee confirms a pending order.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>The result is not null.</li>
     * <li>The returned status is "CONFIRMED".</li>
     * <li>The {@code orderRepository.save()} is called with an order that has the CONFIRMED status and the assigned Employee.</li>
     * <li>The {@code emailService.sendOrderConfirmation()} is called to notify the client.</li>
     * </ul>
     */
    @Test
    void confirmOrder_ShouldUpdateStatus_WhenOrderExists() {
        // Arrange
        Long orderId = 1L;
        String employeeEmail = "employee@test.com";

        Order pendingOrder = Order.builder()
                                  .id(orderId)
                                  .status(OrderStatus.PENDING)
                                  .client(new Client())
                                  .build();

        Employee employee = Employee.builder()
                                    .email(employeeEmail)
                                    .role(Role.EMPLOYEE)
                                    .build();

        Order confirmedOrder = Order.builder()
                                    .id(orderId)
                                    .status(OrderStatus.CONFIRMED)
                                    .employee(employee)
                                    .build();

        OrderDTO expectedDTO = OrderDTO.builder().id(orderId).status("CONFIRMED").build();

        // Mocks
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(employeeRepository.findByEmail(employeeEmail)).thenReturn(Optional.of(employee));
        when(orderRepository.save(any(Order.class))).thenReturn(confirmedOrder);
        when(modelMapper.map(confirmedOrder, OrderDTO.class)).thenReturn(expectedDTO);

        // Act
        OrderDTO result = orderService.confirmOrder(orderId, employeeEmail);

        // Assert
        assertNotNull(result);
        assertEquals("CONFIRMED", result.getStatus());

        // Verify state change
        verify(orderRepository).save(argThat(order ->
                                                     order.getStatus() == OrderStatus.CONFIRMED &&
                                                     order.getEmployee().equals(employee)
        ));

        // Verify email was sent
        verify(emailService).sendOrderConfirmation(result);
    }
}