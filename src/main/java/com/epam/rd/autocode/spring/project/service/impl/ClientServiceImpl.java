package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

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
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new AlreadyExistException("Client already exists with email: " + client.getEmail());
        }
        Client newClient = modelMapper.map(client, Client.class);

        Client savedClient = clientRepository.save(newClient);
        return modelMapper.map(savedClient, ClientDTO.class);
    }
}
