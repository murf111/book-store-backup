package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.model.ShoppingCart;

import java.math.BigDecimal;

/**
 * Service interface for managing shopping carts and their items.
 *
 * <p>Provides operations for:</p>
 * <ul>
 * <li>Retrieving the current user's cart</li>
 * <li>Adding and removing books</li>
 * <li>Adjusting item quantities</li>
 * <li>Price calculations</li>
 * </ul>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 * @see CartDTO
 * @see ShoppingCart
 */
public interface CartService {

    /**
     * Retrieves the shopping cart for a specific client.
     *
     * @param clientEmail the email of the client
     * @return the cart DTO containing all items and total price of them
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the client is not found
     */
    CartDTO getCart(String clientEmail);

    /**
     * Adds a book to the user's cart.
     * <p>If the book is already in the cart, its quantity is incremented.</p>
     *
     * @param userEmail the email of the user
     * @param bookId the ID of the book to add
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the book or user is not found
     */
    void addBook(String userEmail, Long bookId);

    void decreaseBookQuantity(String userEmail, Long bookId);

    /**
     * Removes a book completely from the cart regardless of quantity.
     *
     * @param userEmail the email of the user
     * @param bookId the ID of the book to remove
     */
    void removeBook(String userEmail, Long bookId);

    /**
     * Removes all items from the user's shopping cart.
     *
     * @param userEmail the email of the user
     */
    void clearCart(String userEmail);

    /**
     * Calculates the total price of all items in the given cart.
     *
     * @param cart the shopping cart entity
     * @return the sum of (price * quantity) for all items
     */
    BigDecimal calculateTotalPrice(ShoppingCart cart);
}
