package com.renstapce.review.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtTokenProvider {
    private final String secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("Token is expired: {}", token);
            return true;
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            boolean isValid = username.equals(extractedUsername) && !isTokenExpired(token);
            log.debug("Token validation for user {}: {}", username, isValid ? "valid" : "invalid");
            return isValid;
        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = parseClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return roles != null ? roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList() : Collections.emptyList();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", token);
            throw e;
        } catch (JwtException e) {
            log.error("Failed to parse token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}
