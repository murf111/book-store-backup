package com.epam.rd.autocode.spring.project.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // For public (registration & database)
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/content/**").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/**", "/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/clients").permitAll()

                        // For employees
                        .requestMatchers("/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/books", "/employees").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/books/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/clients/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/clients/*/block").hasRole("EMPLOYEE")
                        .requestMatchers("/orders/*/confirm").hasRole("EMPLOYEE")
                        // Restrict the "Add" and "Edit" pages to Employees only
                        .requestMatchers("/books/add", "/books/*/edit").hasRole("EMPLOYEE")

                        // For clients
                        .requestMatchers(HttpMethod.POST, "/orders").hasRole("CLIENT")
                        .requestMatchers("/cart/**").hasRole("CLIENT")

                        .requestMatchers("/orders/**").authenticated()
                        .requestMatchers("/profile/**").authenticated()

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")           // The URL of your custom page
                        .loginProcessingUrl("/login")  // The URL where the form POSTs data
                        .defaultSuccessUrl("/", true)  // Redirect to Home after success
                        .failureUrl("/login?error=true") // Redirect here on bad password
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
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
