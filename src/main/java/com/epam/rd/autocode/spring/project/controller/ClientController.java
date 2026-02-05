package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.epam.rd.autocode.spring.project.util.Routes.CLIENTS;

@Controller
@RequestMapping(CLIENTS)
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return ViewNames.VIEW_CLIENTS;
    }

    @GetMapping("/{email}")
    public ClientDTO getClientByEmail(@PathVariable String email) {
        return clientService.getClientByEmail(email);
    }

    @PostMapping
    public String addClient(@ModelAttribute @Valid ClientDTO clientDTO) {
        ClientDTO client = clientService.addClient(clientDTO);
        return ViewNames.REDIRECT_LOGIN;
    }

    @PatchMapping("/{email}")
    public ClientDTO updateClientByEmail(@PathVariable String email, @ModelAttribute @Valid ClientDTO clientDTO) {
        return clientService.updateClientByEmail(email, clientDTO);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteClientByEmail(@PathVariable String email) {
        clientService.deleteClientByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{email}/block")
    public String blockClient(@PathVariable String email, @RequestParam boolean block) {
        clientService.blockClient(email, block);
        return ViewNames.REDIRECT_CLIENTS;
    }
}
