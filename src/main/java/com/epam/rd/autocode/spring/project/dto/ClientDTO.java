package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO{
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @ToString.Exclude
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private Boolean isBlocked;
}
