package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.PaymentRequestDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.exception.PaymentDeclinedException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repository.ClientRepository;
import com.epam.rd.autocode.spring.project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public void processPayment(String userEmail, PaymentRequestDTO request) {
        if (request.getCardNumber().startsWith("0000")) {
            throw new PaymentDeclinedException("profile.topup.error.card_lost_stolen");
        }

        if (isCardExpired(request.getExpirationDate())) {
            throw new PaymentDeclinedException("profile.topup.error.card_expired");
        }

        Client client = clientRepository.findByEmail(userEmail)
                                        .orElseThrow(() -> new
                                                NotFoundException("Client" + userEmail + "was not found"));

        client.setBalance(client.getBalance().add(request.getAmount()));
        clientRepository.save(client);
    }

    // Helper method to parse MM/YY and compare
    private boolean isCardExpired(String expirationDate) {
        try {
            String[] parts = expirationDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;

            YearMonth cardExpiry = YearMonth.of(year, month);
            YearMonth currentMonth = YearMonth.now();

            // Returns true if expiry is strictly before now
            return cardExpiry.isBefore(currentMonth);

        } catch (Exception e) {
            return true;
        }
    }
}
