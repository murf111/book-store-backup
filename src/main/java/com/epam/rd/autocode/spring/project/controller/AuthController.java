package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.RegistrationDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

import static com.epam.rd.autocode.spring.project.util.Routes.REGISTER;

@Controller
@RequestMapping(REGISTER)
@RequiredArgsConstructor
public class AuthController {

    private final ClientService clientService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new RegistrationDTO());
        return ViewNames.VIEW_REGISTER;
    }

    @PostMapping
    public String registerClient(@ModelAttribute("client") @Valid RegistrationDTO registrationDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.VIEW_REGISTER;
        }

        try {
            ClientDTO clientDTO = ClientDTO.builder()
                                           .name(registrationDTO.getName())
                                           .email(registrationDTO.getEmail())
                                           .password(registrationDTO.getPassword())
                                           .balance(BigDecimal.ZERO)
                                           .isBlocked(false)
                                           .build();

            clientService.addClient(clientDTO);
        } catch (AlreadyExistException e) {
            bindingResult.rejectValue("email", "error.client", "An account already exists for this email.");
            return ViewNames.VIEW_REGISTER;
        }

        return ViewNames.REDIRECT_LOGIN + "?registerSuccess";
    }
}
