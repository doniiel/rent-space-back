package com.rentspace.userservice.jwt;

import com.rentspace.userservice.exception.InvalidCredentialsException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-expiration-time}")
    private long accessExpirationTimeMs;

    @Value("${jwt.refresh-expiration-time}")
    private long refreshExpirationTimeMs;

    @Value("${jwt.signature-algorithm}")
    private String signatureAlgorithm;

    public String generateAccessToken(String username, Map<String, Object> claims, String role) {
        claims.put("role", role);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTimeMs))
                .addClaims(claims)
                .signWith(SignatureAlgorithm.valueOf(signatureAlgorithm), secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTimeMs))
                .signWith(SignatureAlgorithm.valueOf(signatureAlgorithm), secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            log.error("Failed to extract username from token", e);
            throw new InvalidCredentialsException("Invalid token");
        }
    }

    public String extractRole(String token) {
        try {
            return (String) Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role");
        } catch (JwtException e) {
            log.error("Failed to extract role from token", e);
            throw new InvalidCredentialsException("Invalid token");
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException e) {
            log.error("Failed to check token expiration", e);
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return username.equals(extractedUsername) && !isTokenExpired(token);
        } catch (JwtException e) {
            log.error("Failed to validate token", e);
            return false;
        }
    }
}
