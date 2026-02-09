package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.security.JwtUtil;
import com.epam.rd.autocode.spring.project.service.LoginAttemptService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.epam.rd.autocode.spring.project.util.Routes.HOME;
import static com.epam.rd.autocode.spring.project.util.Routes.LOGIN;

@Controller
@RequestMapping(LOGIN)
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;
    @GetMapping
    public String login() {
        return ViewNames.VIEW_LOGIN;
    }

    @PostMapping
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response,
                        Model model) {
        try {
            // 1. Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 2. SUCCESS: Reset attempts
            loginAttemptService.loginSucceeded(username);

            // 3. Generate Token & Cookie
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            Cookie cookie = new Cookie("accessToken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ViewNames.REDIRECT_HOME;

        } catch (BadCredentialsException e) {
            // 1. Record the failure in DB
            loginAttemptService.loginFailed(username);

            // 2. Ask service for current status
            int remaining = loginAttemptService.getRemainingAttempts(username);
            long lockTime = loginAttemptService.getRemainingLockTime(username);

            // 3. Construct Message based on Service response
            if (lockTime > 0) {
                model.addAttribute("error", "Account locked. Try again in " + lockTime + " minutes.");
            } else {
                model.addAttribute("error", "Invalid credentials. " + remaining + " attempts left.");
            }

            return ViewNames.VIEW_LOGIN;

        } catch (LockedException e) {
            // This catches the 'Client.isBlocked' (Admin block) if UserDetailsService throws it
            model.addAttribute("error", "Your account has been blocked by an administrator.");
            return ViewNames.VIEW_LOGIN;
        }
    }
}