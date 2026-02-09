package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.UserDTO;

import java.time.LocalDate;

/**
 * Service interface for general User account management.
 *
 * <p>Provides common operations applicable to all user roles (Client, Employee):</p>
 * <ul>
 * <li>Retrieving current authenticated user</li>
 * <li>Updating personal data</li>
 * <li>Password management</li>
 * </ul>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 * @see UserDTO
 */
public interface UserService {

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @return the user DTO
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the current user context is invalid
     */
    UserDTO getCurrentUser();

    /**
     * Updates the personal information of a user.
     *
     * @param email the email identifying the user
     * @param name new name
     * @param phone new phone number
     * @param birthDate new birth date
     */
    void updatePersonalData(String email, String name, String phone, LocalDate birthDate);

    /**
     * Changes the user's password.
     *
     * @param currentPassword the raw current password for verification
     * @param newPassword the raw new password to set
     * @return true if the password was changed successfully, false if verification failed
     */
    boolean changePassword(String currentPassword, String newPassword);

    /**
     * Deletes the currently authenticated user account.
     */
    void deleteCurrentUser();
}
