package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.dto.CartItemDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.CartItem;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.ShoppingCart;
import com.epam.rd.autocode.spring.project.repository.BookRepository;
import com.epam.rd.autocode.spring.project.repository.CartRepository;
import com.epam.rd.autocode.spring.project.repository.ClientRepository;
import com.epam.rd.autocode.spring.project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CartDTO getCart(String clientEmail) {
        ShoppingCart cartEntity = getCartEntity(clientEmail);

        CartDTO cartDTO = modelMapper.map(cartEntity, CartDTO.class);

        BigDecimal total = cartDTO.getItems().stream()
                                  .map(CartItemDTO::getTotalPrice)
                                  .reduce(BigDecimal.ZERO, BigDecimal::add);
        cartDTO.setTotalPrice(total);

        return cartDTO;
    }

    @Override
    @Transactional
    public void addBook(String clientEmail, Long bookId) {
        ShoppingCart cart = getCartEntity(clientEmail);

        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new NotFoundException("Book not found"));

        // Check if book already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                                              .filter(item -> item.getBook().getId().equals(bookId))
                                              .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            CartItem newItem = CartItem.builder()
                                       .shoppingCart(cart)
                                       .book(book)
                                       .quantity(1)
                                       .build();
            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void decreaseBookQuantity(String clientEmail, Long bookId) {
        ShoppingCart cart = getCartEntity(clientEmail);

        // Find the specific item
        Optional<CartItem> existingItem = cart.getItems().stream()
                                         .filter(item -> item.getBook().getId().equals(bookId))
                                         .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            // If more than 1, decrease. If 1, remove it.
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.getItems().remove(item);
            }
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void removeBook(String clientEmail, Long bookId) {
        ShoppingCart cart = getCartEntity(clientEmail);
        cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(String clientEmail) {
        ShoppingCart cart = getCartEntity(clientEmail);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Helper method to keep code DRY
    private ShoppingCart getCartEntity(String clientEmail) {
        return cartRepository.findByClientEmail(clientEmail)
                             .orElseGet(() -> createCartForUser(clientEmail));
    }

    private ShoppingCart createCartForUser(String email) {
        Client client = clientRepository.findByEmail(email)
                                        .orElseThrow(() -> new NotFoundException("Client not found: " + email));
        ShoppingCart cart = ShoppingCart.builder().client(client).build();
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateTotalPrice(ShoppingCart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) return BigDecimal.ZERO;
        return cart.getItems().stream()
                   .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                   .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}