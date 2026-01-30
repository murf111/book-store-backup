package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.UserDTO;
import com.epam.rd.autocode.spring.project.model.User;
import com.epam.rd.autocode.spring.project.repo.UserRepository;
import com.epam.rd.autocode.spring.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getCurrentUser() {
        User user = getUserEntity();

        // Manual mapping to ensure total decoupling (Enum -> String)
        return UserDTO.builder()
                      .id(user.getId())
                      .email(user.getEmail())
                      .name(user.getName())
                      .role(user.getRole().name())
                      .build();
    }

    @Override
    public void updateName(String newName) {
        User user = getUserEntity();
        user.setName(newName);
        userRepository.save(user);
    }

    @Override
    public boolean changePassword(String currentPassword, String newPassword) {
        User user = getUserEntity();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void deleteCurrentUser() {
        userRepository.delete(getUserEntity());
    }

    private User getUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
