package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;

import java.util.List;

/**
 * Service interface for managing customer orders.
 *
 * <p>Handles the lifecycle of an order from creation to confirmation:</p>
 * <ul>
 * <li>Order placement (checkout)</li>
 * <li>Order history retrieval</li>
 * <li>Order status updates</li>
 * </ul>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 * @see OrderDTO
 */
public interface OrderService {

    List<OrderDTO> getAllOrders();

    /**
     * Retrieves the order history for a specific client.
     *
     * @param clientEmail the email of the client
     * @return list of orders belonging to the client
     */
    List<OrderDTO> getOrdersByClient(String clientEmail);

    /**
     * Retrieves orders associated with a specific employee.
     *
     * @param employeeEmail the email of the employee
     * @return list of orders processed by the employee
     */
    List<OrderDTO> getOrdersByEmployee(String employeeEmail);

    /**
     * Places a new order based on the client's current shopping cart.
     *
     * @param order the order details (delivery address, etc.)
     * @return the created order DTO
     * @throws InsufficientFundsException if the cart is empty or balance is insufficient
     */
    OrderDTO addOrder(OrderDTO order);

    /**
     * Confirms an order and updates its status.
     *
     * <p>Requires EMPLOYEE role.</p>
     *
     * @param orderId the ID of the order to confirm
     * @param email the email of the employee confirming the order
     * @return the updated order DTO
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the order is not found
     */
    OrderDTO confirmOrder(Long orderId, String email);
}
