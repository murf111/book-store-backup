package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async// Runs in background so the UI doesn't lag
    @Override
    public void sendOrderConfirmation(OrderDTO order) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(senderEmail);

        message.setSubject("New Order Confirmed: #" + order.getId());

        // Create a detailed list of books for the email body
        String itemsList = order.getBookItems().stream()
                                .map(item -> "- " + item.getBookName() + " (x" + item.getQuantity() + ")")
                                .collect(Collectors.joining("\n"));

        String body = String.format(
                "Hello,\n\nAn order has been confirmed!\n\n" +
                "Customer: %s\n" +
                "Address: %s, %s\n\n" +
                "Books Purchased:\n%s\n\n" +
                "Total Paid: $%s\n\n" +
                "Status: %s",
                order.getClientEmail(),
                order.getDeliveryAddress(),
                order.getCity(),
                itemsList,
                order.getPrice().toString(),
                order.getStatus()
        );

        message.setText(body);

        try {
            mailSender.send(message);
            log.info("Order confirmation email sent for Order ID: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to send confirmation email for Order ID: {}", order.getId(), e);
        }
    }

    @Override
    public void sendPasswordRecovery(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}