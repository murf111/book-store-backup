package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO{
    @NotBlank
    @Email
    private String clientEmail;

    @Email
    private String employeeEmail;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private BigDecimal price;

    @NotEmpty
    private List<BookItemDTO> bookItems;
}
