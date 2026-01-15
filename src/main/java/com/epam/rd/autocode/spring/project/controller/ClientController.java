package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{email}")
    public ClientDTO getClientByEmail(@PathVariable String email) {
        return clientService.getClientByEmail(email);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> addClient(@RequestBody @Valid ClientDTO clientDTO) {
        ClientDTO client = clientService.addClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PatchMapping("/{email}")
    public ClientDTO updateClientByEmail(@PathVariable String email, @RequestBody @Valid ClientDTO clientDTO) {
        return clientService.updateClientByEmail(email, clientDTO);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteClientByEmail(@PathVariable String email) {
        clientService.deleteClientByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
