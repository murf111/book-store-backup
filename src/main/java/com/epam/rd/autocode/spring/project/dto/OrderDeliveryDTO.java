package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderDeliveryDTO {

    @NotBlank(message = "{validation.order.address_required}")
    private String deliveryAddress;

    @NotBlank(message = "{validation.order.city_required}")
    private String city;

    @NotBlank(message = "{validation.order.postal_required}")
    private String postalCode;
}