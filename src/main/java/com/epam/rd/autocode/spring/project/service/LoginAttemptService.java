package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.model.User;

public interface LoginAttemptService {

    void loginFailed(String email);

    void loginSucceeded(String email);

    boolean isBlocked(User user);

    int getRemainingAttempts(String email);

    long getRemainingLockTime(String email);
}
