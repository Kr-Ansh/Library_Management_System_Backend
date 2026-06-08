package com.irons.library_management_system_backend.security;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {

                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                boolean isAdmin = Jwts.parser()
                        .verifyWith(jwtUtils.getSigningKey())
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .get("isAdmin", Boolean.class);

                List<SimpleGrantedAuthority> authorities;

                if(isAdmin) {
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    logger.info("🔑 Authenticated admin thread context for user: {}", username);
                } else {
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                    logger.info("👤 Authenticated patron thread context for user: {}", username);
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication session context: {}", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
