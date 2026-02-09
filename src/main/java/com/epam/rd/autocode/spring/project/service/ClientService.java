package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;

import java.util.List;

/**
 * Service interface for managing Client accounts.
 *
 * <p>Provides operations for:</p>
 * <ul>
 * <li>Client registration (creation)</li>
 * <li>Profile retrieval and updates</li>
 * <li>Administrative blocking/unblocking</li>
 * </ul>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 * @see ClientDTO
 */
public interface ClientService {

    List<ClientDTO> getAllClients();

    /**
     * Retrieves a specific client by email.
     *
     * @param email the client's email
     * @return the client DTO
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the client is not found
     */
    ClientDTO getClientByEmail(String email);

    /**
     * Updates a client's profile information.
     *
     * @param email the email identifying the client
     * @param client the updated client data
     * @return the updated client DTO
     */
    ClientDTO updateClientByEmail(String email, ClientDTO client);

    /**
     * Deletes a client account from the system.
     *
     * <p>Client does it himself</p>
     *
     * @param email the email of the client to delete
     */
    void deleteClientByEmail(String email);

    /**
     * Registers a new client in the system.
     *
     * @param client the client registration data
     * @return the created client DTO
     * @throws com.epam.rd.autocode.spring.project.exception.AlreadyExistException
     * if a user with the given email already exists
     */
    ClientDTO addClient(ClientDTO client);

    /**
     * Blocks or unblocks a client account.
     *
     * <p>Requires EMPLOYEE role.</p>
     *
     * @param email the email of the client
     * @param isBlocked true to block, false to unblock
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the client is not found
     */
    void blockClient(String email, boolean isBlocked);
}
