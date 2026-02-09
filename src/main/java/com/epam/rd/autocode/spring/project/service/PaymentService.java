package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.PaymentRequestDTO;

/**
 * Service interface for processing payments and balance top-ups.
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 */
public interface PaymentService {

    /**
     * Processes a payment transaction to top up a user's balance.
     *
     * @param userEmail the email of the user
     * @param request the payment details (card info, amount)
     * @throws com.epam.rd.autocode.spring.project.exception.PaymentDeclinedException if the payment details are invalid
     */
    void processPayment(String userEmail, PaymentRequestDTO request);
}
