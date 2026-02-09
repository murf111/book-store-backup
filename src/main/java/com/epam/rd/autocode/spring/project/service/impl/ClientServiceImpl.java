package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.ShoppingCart;
import com.epam.rd.autocode.spring.project.repository.CartRepository;
import com.epam.rd.autocode.spring.project.repository.ClientRepository;
import com.epam.rd.autocode.spring.project.repository.UserRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ClientService}.
 * <p>
 * Manages the registration, retrieval, and modification of Customer accounts.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                             .stream()
                             .map(client -> modelMapper.map(client, ClientDTO.class))
                             .toList();
    }

    @Override
    public ClientDTO getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                             .map(client -> modelMapper.map(client, ClientDTO.class))
                             .orElseThrow(() -> new NotFoundException
                                     ("Client with email " + email + " was not found"));
    }

    @Override
    @Transactional
    public ClientDTO updateClientByEmail(String email, ClientDTO client) {
        Client existingClient = clientRepository.findByEmail(email)
                                              .orElseThrow(() -> new NotFoundException
                                                  ("Client with email " + email + " was not found"));

        modelMapper.map(client, existingClient);

        Client savedClient = clientRepository.save(existingClient);

        return modelMapper.map(savedClient, ClientDTO.class);
    }

    @Override
    @Transactional
    public void deleteClientByEmail(String email) {
        clientRepository.delete(clientRepository.findByEmail(email)
                                                .orElseThrow(() -> new NotFoundException
                                                        ("Client with email " + email + " was not found")));
    }

    @Override
    @Transactional
    public ClientDTO addClient(ClientDTO client) {
        if (userRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new AlreadyExistException("User already exists with email: " + client.getEmail());
        }
        Client newClient = modelMapper.map(client, Client.class);
        newClient.setPassword(passwordEncoder.encode(newClient.getPassword()));

        ShoppingCart cart = ShoppingCart.builder()
                                        .client(newClient)
                                        .build();

        newClient.setShoppingCart(cart);

        Client savedClient = clientRepository.save(newClient);

        return modelMapper.map(savedClient, ClientDTO.class);
    }

    @Override
    @Transactional
    public void blockClient(String email, boolean isBlocked) {
        Client client = clientRepository.findByEmail(email)
                                        .orElseThrow(() -> new NotFoundException
                                                ("Client with email " + email + " was not found"));
        client.setIsBlocked(isBlocked);
        clientRepository.save(client);
    }
}
