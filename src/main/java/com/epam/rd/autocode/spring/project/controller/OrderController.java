package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.OrderService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.epam.rd.autocode.spring.project.util.Routes.ORDERS;

@Controller
@RequestMapping(ORDERS)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String getOrders(Model model, Authentication authentication) {
        String email = authentication.getName();

        if (authentication.getAuthorities().stream()
                          .anyMatch(a -> a.getAuthority()
                                          .equals("ROLE_EMPLOYEE"))) {
            model.addAttribute("orders", orderService.getAllOrders());
        } else {
            model.addAttribute("orders", orderService.getOrdersByClient(email));
        }
        return ViewNames.VIEW_ORDERS;
    }

    @GetMapping("/client/{clientEmail}")
    public String getOrdersByClient(@PathVariable String clientEmail, Model model) {
        model.addAttribute("orders", orderService.getOrdersByClient(clientEmail));
        return ViewNames.VIEW_ORDERS;
    }

    @GetMapping("/employee/{employeeEmail}")
    public String getOrdersByEmployee(@PathVariable String employeeEmail, Model model) {
        model.addAttribute("orders", orderService.getOrdersByEmployee(employeeEmail));
        return ViewNames.VIEW_ORDERS;
    }

    @PostMapping
    public String addOrder(@RequestParam @Valid OrderDTO orderDTO) {
        OrderDTO order = orderService.addOrder(orderDTO);
        return ViewNames.REDIRECT_ORDERS;
    }

    @PatchMapping("/{id}/confirm")
    public String confirmOrder(@PathVariable Long id, Authentication authentication) {
        String employeeEmail = authentication.getName();
        orderService.confirmOrder(id, employeeEmail);
        return ViewNames.REDIRECT_ORDERS;
    }
}
