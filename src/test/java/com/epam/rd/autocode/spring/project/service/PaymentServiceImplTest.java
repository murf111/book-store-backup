package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.PaymentRequestDTO;
import com.epam.rd.autocode.spring.project.exception.PaymentDeclinedException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repository.ClientRepository;
import com.epam.rd.autocode.spring.project.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private ClientRepository clientRepository;
    @InjectMocks private PaymentServiceImpl paymentService;

    @Test
    void processPayment_ShouldAddBalance_WhenCardIsValid() {
        // Arrange
        String email = "client@example.com";
        Client client = new Client();
        client.setBalance(new BigDecimal("10.00"));

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setAmount(new BigDecimal("50.00"));
        request.setCardNumber("1234567812345678"); // Valid number

        // Generate a future date (e.g., current year + 2)
        int futureYear = YearMonth.now().getYear() + 2 - 2000;
        request.setExpirationDate("12/" + futureYear);
        request.setCvv("123");

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        // Act
        paymentService.processPayment(email, request);

        // Assert
        assertEquals(new BigDecimal("60.00"), client.getBalance());
        verify(clientRepository).save(client);
    }

    @Test
    void processPayment_ShouldThrow_WhenCardNumberInvalid() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setCardNumber("0000123456789012"); // "0000" prefix triggers decline
        request.setAmount(BigDecimal.TEN);

        // Act & Assert
        assertThrows(PaymentDeclinedException.class,
                     () -> paymentService.processPayment("user@example.com", request));
    }

    @Test
    void processPayment_ShouldThrow_WhenCardExpired() {
        // Arrange
        String email = "client@example.com";

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setCardNumber("1234567812345678");
        request.setExpirationDate("01/20"); // Expired date (Jan 2020)
        request.setAmount(BigDecimal.TEN);

        // Act & Assert
        assertThrows(PaymentDeclinedException.class,
                     () -> paymentService.processPayment(email, request));
    }
}