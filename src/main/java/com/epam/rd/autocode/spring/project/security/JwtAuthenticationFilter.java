package com.epam.rd.autocode.spring.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Custom security filter that intercepts every HTTP request to validate JWTs.
 *
 * <p>Workflow:</p>
 * <ol>
 * <li>Checks for the "accessToken" cookie in the request.</li>
 * <li>If found, extracts and validates the JWT using {@link JwtUtil}.</li>
 * <li>If valid, authenticates the user in the Spring {@link SecurityContextHolder}.</li>
 * <li>Passes the request down the filter chain.</li>
 * </ol>
 *
 * <p>This filter executes once per request.</p>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Core filter logic to authenticate requests via JWT.
     *
     * @param request incoming HTTP request
     * @param response outgoing HTTP response
     * @param filterChain the Spring Security filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = null;
        String userEmail = null;

        // 1. Extract Token from Cookie
        if (request.getCookies() != null) {
            jwt = Arrays.stream(request.getCookies())
                        .filter(c -> "accessToken".equals(c.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
        }

        // 2. If no token found, continue the chain (anonymous user)
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract Username (Email)
        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Token invalid or expired - clear context to force re-login
            SecurityContextHolder.clearContext();
        }

        // 4. Validate Token & Set Authentication
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // This is the magic moment: User is now "Logged In" for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}