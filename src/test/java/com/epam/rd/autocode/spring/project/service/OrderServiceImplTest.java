package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private BookRepository bookRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

//    @Test
//    void confirmOrder_ShouldUpdateStatus_WhenOrderExists() {
//        // Arrange
//        Long orderId = 1L;
//        String employeeEmail = "employee@test.com";
//
//        Order pendingOrder = Order.builder()
//                                  .id(orderId)
//                                  .status(OrderStatus.PENDING)
//                                  .build();
//
//        Employee employee = Employee.builder()
//                                    .email(employeeEmail)
//                                    .role(com.epam.rd.autocode.spring.project.model.enums.Role.EMPLOYEE)
//                                    .build();
//
//        Order confirmedOrder = Order.builder()
//                                    .id(orderId)
//                                    .status(OrderStatus.CONFIRMED)
//                                    .build();
//
//        OrderDTO expectedDTO = OrderDTO.builder().build(); // Simplified for test
//
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
//
//        when(employeeRepository.findByEmail(employeeEmail)).thenReturn(Optional.of(employee));
//
//        when(orderRepository.save(any(Order.class))).thenReturn(confirmedOrder);
//        when(modelMapper.map(confirmedOrder, OrderDTO.class)).thenReturn(expectedDTO);
//
//        // Act
//        OrderDTO result = orderService.confirmOrder(orderId, employeeEmail);
//
//        // Assert
//        assertNotNull(result);
//        verify(orderRepository).save(argThat(order ->
//                                                     order.getStatus() == OrderStatus.CONFIRMED
//        ));
@Test
void confirmOrder_ShouldUpdateStatus_WhenOrderExists() {
    // Arrange
    Long orderId = 1L;
    String employeeEmail = "employee@test.com"; // 1. Define the email

    // 2. Mock the Order
    Order pendingOrder = Order.builder()
                              .id(orderId)
                              .status(OrderStatus.PENDING)
                              .build();

    // 3. Mock the Employee (Crucial Step missing in your failure)
    Employee employee = Employee.builder()
                                .email(employeeEmail)
                                .role(com.epam.rd.autocode.spring.project.model.enums.Role.EMPLOYEE)
                                .build();

    Order confirmedOrder = Order.builder()
                                .id(orderId)
                                .status(OrderStatus.CONFIRMED)
                                .employee(employee)
                                .build();

    OrderDTO expectedDTO = OrderDTO.builder().build();

    // 4. Define Mock Behavior
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));

    // [FIX] You must tell the mock to return the employee when asked!
    when(employeeRepository.findByEmail(employeeEmail)).thenReturn(Optional.of(employee));

    when(orderRepository.save(any(Order.class))).thenReturn(confirmedOrder);
    when(modelMapper.map(confirmedOrder, OrderDTO.class)).thenReturn(expectedDTO);

    // Act
    // Pass the email to the method
    OrderDTO result = orderService.confirmOrder(orderId, employeeEmail);

    // Assert
    assertNotNull(result);

    // Verify the status was changed AND the employee was assigned
    verify(orderRepository).save(argThat(order ->
                                                 order.getStatus() == OrderStatus.CONFIRMED &&
                                                 order.getEmployee().equals(employee)
    ));
}
}