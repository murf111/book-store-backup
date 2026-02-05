package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.PaymentRequestDTO;

public interface PaymentService {

    void processPayment(String userEmail, PaymentRequestDTO request);
}
