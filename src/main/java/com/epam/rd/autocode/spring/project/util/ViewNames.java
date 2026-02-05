package com.epam.rd.autocode.spring.project.util;

/**
 * Constants for Thymeleaf View names and Redirect strings.
 * Used in Controller return statements.
 */
public final class ViewNames {

    private ViewNames() {
        // Prevent instantiation
    }

    // --- VIEW TEMPLATES (src/main/resources/templates/...) ---

    // General
    public static final String VIEW_HOME = "home";
    public static final String VIEW_GENERAL_ERROR = "error/error";
    public static final String VIEW_ERROR_404 = "error/404";

    // Auth
    public static final String VIEW_LOGIN = "login";
    public static final String VIEW_REGISTER = "register";

    // Books
    public static final String VIEW_BOOKS_LIST = "books/list";
    public static final String VIEW_BOOKS_ADD = "books/add";
    public static final String VIEW_BOOKS_DETAIL = "books/detail";
    public static final String VIEW_BOOKS_EDIT = "books/edit";

    // Cart
    public static final String VIEW_CART = "cart";
    public static final String VIEW_ORDER_CONFIRM = "order-confirm";

    // Orders
    public static final String VIEW_ORDERS = "orders";
    public static final String VIEW_ORDERS_LIST = "orders/list";
    public static final String VIEW_ORDER_SUCCESS = "orders/success"; // If you have a specific success page

    // Clients
    public static final String VIEW_CLIENTS = "clients";

    // Staff
    public static final String VIEW_STAFF = "staff";
    public static final String VIEW_STAFF_DASHBOARD = "staff/dashboard";
    public static final String VIEW_STAFF_ADD_EMPLOYEE = "staff/add-employee";

    // Password Recovery
    public static final String VIEW_PASSWORD_FORGOT = "password/forgot";
    public static final String VIEW_PASSWORD_RESET = "password/reset";

    // Profile
    public static final String VIEW_PROFILE = "profile";

    // --- REDIRECT COMMANDS ---
    // Usage: return REDIRECT_BOOKS;

    public static final String REDIRECT_HOME = "redirect:" + Routes.HOME;
    public static final String REDIRECT_LOGIN = "redirect:" + Routes.LOGIN;
    public static final String REDIRECT_BOOKS = "redirect:" + Routes.BOOKS;
    public static final String REDIRECT_CART = "redirect:" + Routes.CART;
    public static final String REDIRECT_STAFF = "redirect:" + Routes.STAFF;
    public static final String REDIRECT_ORDERS = "redirect:" + Routes.ORDERS;
    public static final String REDIRECT_CLIENTS = "redirect:" + Routes.CLIENTS;
    public static final String REDIRECT_PROFILE = "redirect:" + Routes.PROFILE;
    public static final String REDIRECT_LOGOUT = "redirect:" + Routes.LOGOUT;
}