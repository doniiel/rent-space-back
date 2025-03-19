package com.rentspace.listingservice.jwt;

import com.rentspace.core.exception.TokenExpiredException;
import com.rentspace.listingservice.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(TokenUtil.HEADER);
        String token = TokenUtil.extractToken(header);

        if (token != null) {
            try {
                processToken(token);
            } catch (JwtException e) {
                log.error("JWT processing failed: {}", e.getMessage());
                if (e instanceof ExpiredJwtException) {
                    throw new TokenExpiredException("Token has expired");
                }
            }
        } else {
            log.debug("No valid Bearer token found in request");
        }

        filterChain.doFilter(request, response);
    }

    private void processToken(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtTokenUtil.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, jwtTokenUtil.extractAuthorities(token)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authentication set for user: {}", username);
            } else {
                log.warn("Invalid token for user: {}", username);
            }
        }
    }
}