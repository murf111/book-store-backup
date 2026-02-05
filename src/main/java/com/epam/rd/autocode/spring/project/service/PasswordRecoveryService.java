package com.epam.rd.autocode.spring.project.service;

public interface PasswordRecoveryService {

    void processForgotPassword(String email);

    void resetPassword(String email, String rawToken, String newPassword);
}
