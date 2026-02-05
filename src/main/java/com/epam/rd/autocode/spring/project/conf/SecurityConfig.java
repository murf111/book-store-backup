package com.epam.rd.autocode.spring.project.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
public class SecurityConfig{

    private final UserDetailsService userDetailsService;
    private final CustomAuthFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // --- Public Resources (DB & Static) ---
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/content/**").permitAll()

                        // --- Authentication ---
                        .requestMatchers(REGISTER, LOGIN).permitAll()
                        .requestMatchers(PASSWORD + "/**").permitAll()

                        // --- Employee Restricted ---
                        .requestMatchers(STAFF + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, BOOKS, STAFF).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, BOOKS + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, BOOKS + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, CLIENTS + "/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, CLIENTS + "/*/block").hasRole("EMPLOYEE")
                        .requestMatchers(ORDERS + "/*/confirm").hasRole("EMPLOYEE")

                        // Employee Pages (Add/Edit)
                        .requestMatchers(BOOKS_ADD, BOOKS + "/*/edit").hasRole("EMPLOYEE")

                        // PLACED HERE BECAUSE IT IS ON TOP ANYBODY CAN ACCESS books/add
                        // --- Public Read Access ---
                        .requestMatchers(HttpMethod.GET, BOOKS + "/**", HOME).permitAll()
                        .requestMatchers(HttpMethod.POST, CLIENTS).permitAll()

                        // --- Client Restricted ---
                        .requestMatchers(HttpMethod.POST, ORDERS).hasRole("CLIENT")
                        .requestMatchers(CART + "/**").hasRole("CLIENT")

                        // --- Authenticated General ---
                        .requestMatchers(ORDERS + "/**").authenticated()
                        .requestMatchers(PROFILE + "/**").authenticated()

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage(LOGIN)           // The URL of custom page
                        .loginProcessingUrl(LOGIN)  // The URL where the form POSTs data
                        .defaultSuccessUrl(HOME, true)  // Redirect to Home after success
                        .failureHandler(failureHandler) // Redirect here on bad password
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT))
                        .logoutSuccessUrl(LOGIN + "?logout=true")
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

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
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
