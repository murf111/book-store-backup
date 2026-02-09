package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;

/**
 * Service interface for sending asynchronous email notifications.
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 */
public interface EmailService {

    /**
     * Sends an order confirmation email containing order details.
     *
     * @param order the order DTO containing items and price
     */
    void sendOrderConfirmation(OrderDTO order);

    /**
     * Sends a generic password recovery email.
     *
     * @param to recipient email address
     * @param subject email subject line
     * @param body email body content
     */
    void sendPasswordRecovery(String to, String subject, String body);
}
