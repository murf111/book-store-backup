package com.epam.rd.autocode.spring.project.conf;

import com.epam.rd.autocode.spring.project.service.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username"); // Matches the input name in login.html
        String redirectUrl = "/login?error=true";

        if (exception instanceof LockedException) {
            // User is ALREADY locked (caught by UserDetailsService)
            long minutesLeft = loginAttemptService.getRemainingLockTime(email);
            redirectUrl = "/login?error=locked&time=" + minutesLeft;
        }
        else if (exception instanceof BadCredentialsException) {
            // Wrong password. Check if this specific failure CAUSED a lock or just reduced attempts.
            // Note: The EventListener runs BEFORE this handler, so the DB is already updated.
            int attemptsLeft = loginAttemptService.getRemainingAttempts(email);

            if (attemptsLeft <= 0) {
                // They just hit the limit
                redirectUrl = "/login?error=locked&time=15";
            } else {
                redirectUrl = "/login?error=bad_credentials&attempts=" + attemptsLeft;
            }
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}