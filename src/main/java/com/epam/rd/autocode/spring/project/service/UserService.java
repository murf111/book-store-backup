package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.UserDTO;

import java.time.LocalDate;

public interface UserService {

    UserDTO getCurrentUser();

    void updatePersonalData(String email, String name, String phone, LocalDate birthDate);

    boolean changePassword(String currentPassword, String newPassword);

    void deleteCurrentUser();
}
