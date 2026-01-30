package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.UserDTO;

public interface UserService {

    UserDTO getCurrentUser();

    void updateName(String newName);

    boolean changePassword(String currentPassword, String newPassword);

    void deleteCurrentUser();
}
