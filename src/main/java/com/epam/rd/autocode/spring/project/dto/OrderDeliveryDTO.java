package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderDeliveryDTO {

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    private String postalCode;
}