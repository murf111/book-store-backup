package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.CartItem;
import com.epam.rd.autocode.spring.project.model.ShoppingCart;
import com.epam.rd.autocode.spring.project.repository.BookRepository;
import com.epam.rd.autocode.spring.project.repository.CartRepository;
import com.epam.rd.autocode.spring.project.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CartServiceImpl}.
 * <p>
 * Tests logic for modifying cart contents, specifically quantity management.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock private CartRepository cartRepository;
    @Mock private BookRepository bookRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    /**
     * Tests the {@link CartServiceImpl#addBook(String, Long)} method.
     * <p>
     * <strong>Scenario:</strong> Adding a book that is already present in the user's cart.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>The quantity of the existing {@code CartItem} is incremented by 1 (from 1 to 2).</li>
     * <li>The {@code cartRepository.save()} method is called to persist the change.</li>
     * </ul>
     */
    @Test
    void addBook_ShouldIncrementQuantity_WhenItemExists() {
        String email = "user@test.com";
        Long bookId = 100L;

        Book book = new Book();
        book.setId(bookId);

        CartItem existingItem = CartItem.builder().book(book).quantity(1).build();
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());
        cart.getItems().add(existingItem);

        when(cartRepository.findByClientEmail(email)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        cartService.addBook(email, bookId);

        assertEquals(2, existingItem.getQuantity());
        verify(cartRepository).save(cart);
    }
}