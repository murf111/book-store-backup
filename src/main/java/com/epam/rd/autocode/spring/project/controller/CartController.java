package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.component.ShoppingCart;
import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final BookService bookService;
    private final ShoppingCart cart;
    private final OrderService orderService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("items", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        BookDTO book = bookService.getBookById(id);
        cart.addBook(book);

        redirectAttributes.addFlashAttribute("cartMessage", "added");

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/books");
    }

    @PostMapping("/checkout")
    public String checkout(Authentication authentication) {
        if (cart.getCartItems().isEmpty()) {
            return "redirect:/cart?error=empty";
        }

        // 1. Map Cart Items to BookItemDTOs
        // NOTE: We are forced to use Name here because BookItemDTO doesn't have an ID field
        List<BookItemDTO> bookItems = cart.getCartItems().stream()
                                          .map(item -> new BookItemDTO(item.getBookDTO().getName(), item.getQuantity()))
                                          .collect(Collectors.toList());

        // 2. Build the Order
        OrderDTO orderDTO = OrderDTO.builder()
                                    .clientEmail(authentication.getName())
                                    .orderDate(LocalDateTime.now())
                                    .price(cart.getTotalPrice())
                                    .status(OrderStatus.PENDING.name()) // Hardcode string or enum.name()
                                    .bookItems(bookItems)
                                    .build();

        // 3. Save to DB
        orderService.addOrder(orderDTO);

        // 4. Clear Cart
        cart.clear();

        return "redirect:/cart"; // for time, cause order isn't implemented
    }

    @DeleteMapping("/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cart.removeBook(id);
        return "redirect:/cart";
    }

    @DeleteMapping("/clear")
    public String clearCart() {
        cart.clear();
        return "redirect:/cart";
    }
}
