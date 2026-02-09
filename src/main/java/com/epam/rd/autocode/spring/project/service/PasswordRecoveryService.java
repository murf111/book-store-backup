package com.epam.rd.autocode.spring.project.service;

/**
 * Service interface for handling the "Forgot Password" workflow.
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 */
public interface PasswordRecoveryService {

    /**
     * Initiates the password recovery process.
     * <p>Generates a recovery token and sends an email if the user exists.</p>
     *
     * @param email the email provided by the user
     */
    void processForgotPassword(String email);

    /**
     * Resets the user's password using a valid recovery token.
     *
     * @param email the user's email
     * @param rawToken the recovery token received via email
     * @param newPassword the new password to set
     * @throws com.epam.rd.autocode.spring.project.exception.InvalidTokenException if the token is invalid or expired
     */
    void resetPassword(String email, String rawToken, String newPassword);
}
