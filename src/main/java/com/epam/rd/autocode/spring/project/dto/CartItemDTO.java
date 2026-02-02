package com.epam.rd.autocode.spring.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private BookDTO book;
    private Integer quantity;

    public BookDTO getBookDTO() {
        return book;
    }

    // Helper for Thymeleaf to calculate row total
    public BigDecimal getTotalPrice() {
        if (book == null || book.getPrice() == null) return BigDecimal.ZERO;
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}