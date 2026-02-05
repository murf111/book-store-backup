package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        // Inject the @Value property manually
        ReflectionTestUtils.setField(emailService, "senderEmail", "admin@bookstore.com");
    }

    @Test
    void sendOrderConfirmation_ShouldSendEmail_WithCorrectDetails() {
        // Arrange
        // [FIX] Use BookItemDTO instead of OrderItemDTO
        BookItemDTO item = new BookItemDTO();
        item.setBookName("Test Book");
        item.setQuantity(2);

        OrderDTO order = OrderDTO.builder()
                                 .id(101L)
                                 .clientEmail("client@example.com")
                                 .price(new BigDecimal("50.00"))
                                 .status("CONFIRMED")
                                 .bookItems(List.of(item)) // [FIX] List is not null
                                 .deliveryAddress("123 Main St") // [FIX] Added to match String.format args
                                 .city("New York")
                                 .build();

        // Act
        emailService.sendOrderConfirmation(order);

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        // Assert Sender/Recipient
        assertEquals("admin@bookstore.com", message.getFrom());
        assertEquals("admin@bookstore.com", message.getTo()[0]);

        // Assert Subject
        String subject = message.getSubject();
        assertTrue(subject != null && subject.contains("101"));

        // Assert Body Content
        String body = message.getText();
        assertTrue(body != null);
        assertTrue(body.contains("client@example.com")); // Customer email
        assertTrue(body.contains("123 Main St"));        // Address
        assertTrue(body.contains("50.00"));              // Total Price
        assertTrue(body.contains("Test Book"));          // Item Name
        assertTrue(body.contains("(x2)"));               // Quantity format from your service
    }

    @Test
    void sendPasswordRecovery_ShouldSendEmail_WithCorrectContent() {
        // Act
        emailService.sendPasswordRecovery("user@example.com", "Reset Subject", "Reset Body");

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("user@example.com", message.getTo()[0]);
        assertEquals("Reset Subject", message.getSubject());
        assertEquals("Reset Body", message.getText());
    }
}