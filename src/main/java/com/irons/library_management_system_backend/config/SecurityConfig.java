package com.irons.library_management_system_backend.config;

import com.irons.library_management_system_backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. Disable Cross-Site Request Forgery (CSRF)
                // Why: CSRF protection is primarily for browser-based session cookies.
                // Since we are building a stateless REST API powered by JWTs, we can safely disable it.
                .csrf(AbstractHttpConfigurer::disable)
                // 🔥 CRITICAL: Force Spring Security to remain entirely STATELESS.
                // This prevents Spring from trying to generate or store HTTP sessions in server memory.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 2. Configure Route Authorization Rules
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access Swagger UI and OpenAPI documentation routes without logging in
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html",
                                "/api/auth/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/books/borrow", "/api/books/return").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );

        // 🔥 Tell Spring to execute our custom JWT validation BEFORE checking basic forms
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
