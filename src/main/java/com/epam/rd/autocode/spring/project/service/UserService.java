package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.UserDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserService {

    UserDTO getCurrentUser();

    void updatePersonalData(String email, String name, BigDecimal balance, String phone, LocalDate birthDate);

    boolean changePassword(String currentPassword, String newPassword);

    void deleteCurrentUser();
}
