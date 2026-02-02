package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDeliveryDTO;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.service.CartService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public String showCart(Model model, Authentication authentication) {
        CartDTO cartDTO = cartService.getCart(authentication.getName());

        model.addAttribute("items", cartDTO.getItems());
        model.addAttribute("totalPrice", cartDTO.getTotalPrice());
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, RedirectAttributes redirectAttributes,
                            HttpServletRequest request, Authentication authentication) {
        cartService.addBook(authentication.getName(), id);

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.contains("/cart")) {
            redirectAttributes.addFlashAttribute("cartMessage", "added");
        }

        return "redirect:" + (referer != null ? referer : "/books");
    }

    @PostMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id, Authentication authentication) {
        cartService.decreaseBookQuantity(authentication.getName(), id);
        return "redirect:/cart";
    }


    // ========================================================================
    // NEW CHECKOUT FLOW
    // ========================================================================

    // STEP 1: Show the Confirmation Page
    // This replaces the old "immediate" checkout. Now we show the form first.
    @GetMapping("/checkout")
    public String showCheckout(Model model, Authentication authentication) {
        CartDTO cart = cartService.getCart(authentication.getName());

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart?error=empty";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());
        // Add empty DTO for the form to bind to
        model.addAttribute("orderPlacement", new OrderDeliveryDTO());

        return "order-confirm"; // Returns the new template we created
    }

    // STEP 2: Process the Order
    // This contains the logic from your OLD checkout method, but upgraded
    // to handle the address form and validation.
    @PostMapping("/checkout")
    public String placeOrder(@ModelAttribute @Valid OrderDeliveryDTO placementDTO,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        String email = authentication.getName();
        CartDTO cartDTO = cartService.getCart(email);

        // 1. Handle Validation Errors (e.g. empty address)
        if (bindingResult.hasErrors()) {
            // Reload the page with errors
            model.addAttribute("cart", cartDTO);
            model.addAttribute("totalPrice", cartDTO.getTotalPrice());
            return "order-confirm";
        }

        try {
            // 2. Convert Cart Items to BookItemDTOs (Same logic as before)
            List<BookItemDTO> bookItems = cartDTO.getItems().stream()
                                                 .map(item -> new BookItemDTO(
                                                         item.getBookDTO().getId(),
                                                         item.getBookDTO().getName(),
                                                         item.getQuantity()))
                                                 .collect(Collectors.toList());

            // 3. Build OrderDTO (Now includes Delivery Info!)
            OrderDTO orderDTO = OrderDTO.builder()
                                        .clientEmail(email)
                                        .orderDate(LocalDateTime.now())
                                        .price(cartDTO.getTotalPrice())
                                        .status(OrderStatus.PENDING.name())
                                        .bookItems(bookItems)
                                        // --- NEW FIELDS ---
                                        .deliveryAddress(placementDTO.getDeliveryAddress())
                                        .city(placementDTO.getCity())
                                        .postalCode(placementDTO.getPostalCode())
                                        .build();

            // 4. Call Service (This now handles the balance check)
            orderService.addOrder(orderDTO);

            // 5. Clear Cart
            cartService.clearCart(email);

            return "redirect:/orders?success";

        } catch (RuntimeException e) {
            // 6. Handle "Insufficient Balance" Exception
            // Reload page and show the error message
            model.addAttribute("cart", cartDTO);
            model.addAttribute("totalPrice", cartDTO.getTotalPrice());
            model.addAttribute("balanceError", e.getMessage());
            return "order-confirm";
        }
    }

//    @PostMapping("/checkout")
//    public String checkout(Authentication authentication) {
//        String email = authentication.getName();
//        CartDTO cartDTO = cartService.getCart(email);
//
//        if (cartDTO.getItems().isEmpty()) {
//            return "redirect:/cart?error=empty";
//        }
//
//        List<BookItemDTO> bookItems = cartDTO.getItems().stream()
//                                             .map(item -> new BookItemDTO(
//                                                     item.getBook().getId(),    // Using field 'book' (DTO)
//                                                     item.getBook().getName(),
//                                                     item.getQuantity()))
//                                             .collect(Collectors.toList());
//
//        OrderDTO orderDTO = OrderDTO.builder()
//                                    .clientEmail(email)
//                                    .orderDate(LocalDateTime.now())
//                                    .price(cartDTO.getTotalPrice())
//                                    .status(OrderStatus.PENDING.name())
//                                    .bookItems(bookItems)
//                                    .build();
//
//        orderService.addOrder(orderDTO);
//        cartService.clearCart(email);
//
//        return "redirect:/orders";
//    }

    @DeleteMapping("/{id}")
    public String removeFromCart(@PathVariable Long id, Authentication authentication) {
        cartService.removeBook(authentication.getName(), id);
        return "redirect:/cart";
    }

    @DeleteMapping("/clear")
    public String clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return "redirect:/cart";
    }
}