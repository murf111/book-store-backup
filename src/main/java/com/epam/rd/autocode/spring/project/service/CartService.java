package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.model.ShoppingCart;

import java.math.BigDecimal;

public interface CartService {

    CartDTO getCart(String clientEmail);

    void addBook(String userEmail, Long bookId);

    void decreaseBookQuantity(String userEmail, Long bookId);

    void removeBook(String userEmail, Long bookId);

    void clearCart(String userEmail);

    BigDecimal calculateTotalPrice(ShoppingCart cart);
}
