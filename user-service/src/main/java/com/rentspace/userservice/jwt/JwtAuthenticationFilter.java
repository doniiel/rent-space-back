package com.rentspace.userservice.jwt;

import com.rentspace.userservice.entity.token.Token;
import com.rentspace.userservice.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Collections;
import java.util.Optional;

import static com.rentspace.core.util.TokenUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtService jwtTokenUtil;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = getJwtFromRequest(request);

        if (token != null) {
            authenticationUser(token);
        }

        filterChain.doFilter(request, response);

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HEADER);

        if (token != null && token.startsWith(PREFIX)) {
            return token.substring(PREFIX_LENGTH);
        }
        return null;
    }

    private void authenticationUser(String token) {
        try {
            Optional<Token> storedToken = tokenRepository.findByToken(token);
            if (storedToken.isEmpty() || storedToken.get().isExpired() || storedToken.get().isRevoked()) {
                log.warn("Invalid or revokerd JWT token: " + token);
                return;
            }

            String username = jwtTokenUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: " + e.getMessage());
        } catch (Exception e) {
            log.error("JWT token processing error: " + e.getMessage());
        }
    }
}

