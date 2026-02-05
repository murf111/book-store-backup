package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;

public interface EmailService {

    void sendOrderConfirmation(OrderDTO order);

    void sendPasswordRecovery(String to, String subject, String body);
}
