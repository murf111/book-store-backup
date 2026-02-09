package com.epam.rd.autocode.spring.project.util;

/**
 * Constants for URL paths (Request Mappings).
 * Used in Controllers (@RequestMapping, @GetMapping, etc.) and redirects.
 */
public final class Routes {

    private Routes() {
        // Prevent instantiation
    }

    // --- GENERAL ---
    public static final String HOME = "/";
    public static final String ERROR = "/error";

    // --- AUTHENTICATION ---
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";

    // --- BOOKS ---
    public static final String BOOKS = "/books";
    public static final String BOOKS_SEARCH = "/books/search";
    public static final String BOOKS_ADD = "/books/add";
    public static final String BOOKS_DELETE = "/books/delete"; // If you use path variable, handled in controller

    // --- CART ---
    public static final String CART = "/cart";
    public static final String CART_ADD = "/cart/add";
    public static final String CART_REMOVE = "/cart/remove";
    public static final String CART_CLEAR = "/cart/clear";

    // --- ORDER ---
    public static final String ORDERS = "/orders";
    public static final String ORDERS_PLACE = "/orders/place";

    // --- CLIENTS ---
    public static final String CLIENTS = "/clients";
    public static final String PROFILE = "/profile";

    // --- STAFF / EMPLOYEE ---
    public static final String STAFF = "/staff";
    public static final String STAFF_ADD_EMPLOYEE = "/staff/add-employee";
    public static final String STAFF_ORDERS = "/staff/orders"; // If staff manage orders

    // --- PASSWORD RECOVERY ---
    public static final String PASSWORD = "/password";
    public static final String PASSWORD_FORGOT = "/password/forgot";
    public static final String PASSWORD_RESET = "/password/reset";
}