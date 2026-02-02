package com.epam.rd.autocode.spring.project.conf;

import com.epam.rd.autocode.spring.project.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        // The principal is usually the username/email entered
        String email = (String) event.getAuthentication().getPrincipal();
        loginAttemptService.loginFailed(email);
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        // getName() returns the username/email of the logged-in user
        String email = event.getAuthentication().getName();
        loginAttemptService.loginSucceeded(email);
    }
}