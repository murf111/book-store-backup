package com.epam.rd.autocode.spring.project.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.epam.rd.autocode.spring.project.util.Routes.BOOKS;
import static com.epam.rd.autocode.spring.project.util.Routes.BOOKS_ADD;
import static com.epam.rd.autocode.spring.project.util.Routes.CART;
import static com.epam.rd.autocode.spring.project.util.Routes.CLIENTS;
import static com.epam.rd.autocode.spring.project.util.Routes.HOME;
import static com.epam.rd.autocode.spring.project.util.Routes.LOGIN;
import static com.epam.rd.autocode.spring.project.util.Routes.LOGOUT;
import static com.epam.rd.autocode.spring.project.util.Routes.ORDERS;
import static com.epam.rd.autocode.spring.project.util.Routes.PASSWORD;
import static com.epam.rd.autocode.spring.project.util.Routes.PROFILE;
import static com.epam.rd.autocode.spring.project.util.Routes.REGISTER;
import static com.epam.rd.autocode.spring.project.util.Routes.STAFF;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // 1. STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. ROUTE RULES
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/content/**", "/webjars/**").permitAll()
                        .requestMatchers(REGISTER, LOGIN).permitAll()
                        .requestMatchers(PASSWORD + "/**").permitAll()

                        // Employee
                        .requestMatchers(STAFF + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, BOOKS, STAFF).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, BOOKS + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, BOOKS + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, CLIENTS + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, CLIENTS + "/*/block").hasRole("EMPLOYEE")
                        .requestMatchers(ORDERS + "/*/confirm").hasRole("EMPLOYEE")
                        .requestMatchers(BOOKS_ADD, BOOKS + "/*/edit").hasRole("EMPLOYEE")

                        // Public Read
                        .requestMatchers(HttpMethod.GET, BOOKS + "/**", HOME).permitAll()
                        .requestMatchers(HttpMethod.POST, CLIENTS).permitAll()

                        // Client
                        .requestMatchers(HttpMethod.POST, ORDERS).hasRole("CLIENT")
                        .requestMatchers(CART + "/**").hasRole("CLIENT")

                        // General Authenticated
                        .requestMatchers(ORDERS + "/**").authenticated()
                        .requestMatchers(PROFILE + "/**").authenticated()
                        .anyRequest().authenticated()
                )

                // 3. THE JWT FILTER
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 4. MANUAL LOGOUT (Because I use cookies, must manually delete them)
                .logout(logout -> logout
                        .logoutUrl(LOGOUT)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setHeader("Set-Cookie",
                                               "accessToken=; Path=/; HttpOnly; Max-Age=0; SameSite=Lax; Secure");
                            response.sendRedirect(LOGIN + "?logout=true");
                        })
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                                          response.sendRedirect(LOGIN) // Force Redirect
                        )
                )

                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}