package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "{validation.amount.required}")
    @DecimalMin(value = "1.00", message = "{validation.amount.min}")
    private BigDecimal amount;

    @NotBlank(message = "{validation.card.required}")
    @Pattern(regexp = "^\\d{16}$", message = "{validation.card.invalid}")
    private String cardNumber;

    @NotBlank(message = "{validation.expiry.required}")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "{validation.expiry.invalid}")
    private String expirationDate;

    @NotBlank(message = "{validation.cvv.required}")
    @Pattern(regexp = "^\\d{3}$", message = "{validation.cvv.invalid}")
    private String cvv;
}