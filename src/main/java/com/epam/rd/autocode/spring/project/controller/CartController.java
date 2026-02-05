package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDeliveryDTO;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.service.CartService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.rd.autocode.spring.project.util.Routes.BOOKS;
import static com.epam.rd.autocode.spring.project.util.Routes.CART;

@Controller
@RequestMapping(CART)
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public String showCart(Model model, Authentication authentication) {
        CartDTO cartDTO = cartService.getCart(authentication.getName());

        model.addAttribute("items", cartDTO.getItems());
        model.addAttribute("totalPrice", cartDTO.getTotalPrice());
        return ViewNames.VIEW_CART;
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, RedirectAttributes redirectAttributes,
                            HttpServletRequest request, Authentication authentication) {
        cartService.addBook(authentication.getName(), id);

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.contains(CART)) {
            redirectAttributes.addFlashAttribute("cartMessage", "added");
        }

        return "redirect:" + (referer != null ? referer : BOOKS);
    }

    @PostMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id, Authentication authentication) {
        cartService.decreaseBookQuantity(authentication.getName(), id);
        return ViewNames.REDIRECT_CART;
    }

    // Returns the new template
    @GetMapping("/checkout")
    public String showCheckout(Model model, Authentication authentication) {
        CartDTO cart = cartService.getCart(authentication.getName());

        if (cart.getItems().isEmpty()) {
            return ViewNames.REDIRECT_CART + "?error=empty";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());
        // Add empty DTO for the form to bind to
        model.addAttribute("orderPlacement", new OrderDeliveryDTO());

        return ViewNames.VIEW_ORDER_CONFIRM;
    }

    // Processing the order
    @PostMapping("/checkout")
    public String placeOrder(@ModelAttribute @Valid OrderDeliveryDTO placementDTO,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        String email = authentication.getName();
        CartDTO cartDTO = cartService.getCart(email);

        // Handle Validation Errors (e.g. empty address)
        if (bindingResult.hasErrors()) {
            // Reload the page with errors
            model.addAttribute("cart", cartDTO);
            model.addAttribute("totalPrice", cartDTO.getTotalPrice());
            return ViewNames.VIEW_ORDER_CONFIRM;
        }

        try {
            List<BookItemDTO> bookItems = cartDTO.getItems().stream()
                                                 .map(item -> new BookItemDTO(
                                                         item.getBookDTO().getId(),
                                                         item.getBookDTO().getName(),
                                                         item.getQuantity()))
                                                 .collect(Collectors.toList());

            OrderDTO orderDTO = OrderDTO.builder()
                                        .clientEmail(email)
                                        .orderDate(LocalDateTime.now())
                                        .price(cartDTO.getTotalPrice())
                                        .status(OrderStatus.PENDING.name())
                                        .bookItems(bookItems)
                                        .deliveryAddress(placementDTO.getDeliveryAddress())
                                        .city(placementDTO.getCity())
                                        .postalCode(placementDTO.getPostalCode())
                                        .build();

            orderService.addOrder(orderDTO);
            cartService.clearCart(email);

            return ViewNames.REDIRECT_ORDERS + "?success";

        } catch (InsufficientFundsException e) {
            // Try catch works here because if you redirect, the user loses the address they just typed
            return reloadCheckoutPage(model, cartDTO, placementDTO, e.getMessage());

        } catch (RuntimeException e) {
            // Catch unexpected errors
            return reloadCheckoutPage(model, cartDTO, placementDTO, "Unexpected error: " + e.getMessage());
        }
    }

    @DeleteMapping("/item/{id}")
    public String removeFromCart(@PathVariable Long id, Authentication authentication) {
        cartService.removeBook(authentication.getName(), id);
        return ViewNames.REDIRECT_CART;
    }

    @DeleteMapping("/clear")
    public String clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ViewNames.REDIRECT_CART;
    }

    private String reloadCheckoutPage(Model model, CartDTO cart, OrderDeliveryDTO placementDTO, String errorMessage) {
        // Reload Cart Data
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());

        // CAUSED A BUG DO NOT CHANGE IT
        // Reload Form Data (CRITICAL: Keeps user's typed address)
        model.addAttribute("orderPlacement", placementDTO);

        if (errorMessage != null) {
            model.addAttribute("balanceError", errorMessage);
        }

        return ViewNames.VIEW_ORDER_CONFIRM;
    }
}