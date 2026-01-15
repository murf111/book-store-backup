package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/client/{clientEmail}")
    public List<OrderDTO> getOrdersByClient(@PathVariable String clientEmail) {
        return orderService.getOrdersByClient(clientEmail);
    }

    @GetMapping("/employee/{employeeEmail}")
    public List<OrderDTO> getOrdersByEmployee(@PathVariable String employeeEmail) {
        return orderService.getOrdersByEmployee(employeeEmail);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody @Valid OrderDTO orderDTO) {
        OrderDTO order = orderService.addOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
