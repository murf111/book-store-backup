//package com.epam.rd.autocode.spring.project.component;
//
//import com.epam.rd.autocode.spring.project.dto.BookDTO;
//import com.epam.rd.autocode.spring.project.dto.ClientDTO;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.Getter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.annotation.SessionScope;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@SessionScope
//@Data
//@Getter
//public class ShoppingCart {
//    List<CartItem> cartItems = new ArrayList<>();
//
//    public void addBook(BookDTO bookDTO) {
//        Optional<CartItem> book = cartItems.stream()
//                                              .filter(cartItem -> cartItem.getBookDTO()
//                                                                          .getId()
//                                                                          .equals(bookDTO.getId()))
//                                              .findFirst();
//        if (book.isPresent()) {
//            book.get().incrementQuantity();
//        }
//        else {
//            cartItems.add(new CartItem(bookDTO, 1));
//        }
//    }
//
//    public void removeBook(Long id) {
//        cartItems.removeIf(cartItem -> cartItem.getBookDTO().getId().equals(id));
//    }
//
//    public void clear() {
//        cartItems.clear();
//    }
//
//    public BigDecimal getTotalPrice() {
//        return cartItems.stream()
//                           .map(CartItem::getTotalPrice)
//                           .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    @Data
//    @AllArgsConstructor
//    public static class CartItem {
//        private BookDTO bookDTO;
//        private int quantity;
//
//        public BigDecimal getTotalPrice() {
//            if (bookDTO == null || bookDTO.getPrice() == null) return BigDecimal.ZERO;
//            return bookDTO.getPrice().multiply(BigDecimal.valueOf(quantity));
//        }
//
//        public void incrementQuantity() {
//            this.quantity++;
//        }
//    }
//}
