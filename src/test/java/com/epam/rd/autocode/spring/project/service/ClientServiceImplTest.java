package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repository.CartRepository;
import com.epam.rd.autocode.spring.project.repository.ClientRepository;
import com.epam.rd.autocode.spring.project.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock private ClientRepository clientRepository;
    @Mock private CartRepository cartRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void blockClient_ShouldUpdateStatus() {
        String email = "badguy@test.com";
        Client client = new Client();
        client.setIsBlocked(false);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        clientService.blockClient(email, true);

        assertTrue(client.getIsBlocked());
        verify(clientRepository).save(client);
    }

    @Test
    void blockClient_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> clientService.blockClient("unknown@test.com", true));
    }
}