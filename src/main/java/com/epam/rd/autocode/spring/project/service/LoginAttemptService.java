package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.model.User;

/**
 * Service interface for protecting against brute-force login attacks.
 *
 * <p>Tracks failed login attempts and temporarily locks accounts.</p>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 */
public interface LoginAttemptService {

    /**
     * Records a failed login attempt for the given email.
     * <p>Increments the failure count and may lock the account.</p>
     *
     * @param email the email used in the failed attempt
     */
    void loginFailed(String email);

    /**
     * Resets the failure count upon a successful login.
     *
     * @param email the email of the successfully logged-in user
     */
    void loginSucceeded(String email);

    /**
     * Checks if a user is currently blocked due to too many failed attempts.
     *
     * @param user the user entity to check
     * @return true if the user is locked, false otherwise
     */
    boolean isBlocked(User user);

    /**
     * Retrieves the number of remaining login attempts before lockout.
     *
     * @param email the email to check
     * @return number of remaining attempts
     */
    int getRemainingAttempts(String email);

    /**
     * Retrieves the remaining time until the account is unlocked.
     *
     * @param email the email to check
     * @return remaining minutes, or 0 if not locked
     */
    long getRemainingLockTime(String email);
}
