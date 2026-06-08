package com.irons.library_management_system_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    public SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, boolean isAdmin) {
        return Jwts.builder().subject(username)
                .claim("isAdmin", isAdmin)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT cryptographic signature pattern: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT validation failed: Token window has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT parsing failed: Token format is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT validation claims string context is completely empty: {}", e.getMessage());
        }
        return false;
    }
}
