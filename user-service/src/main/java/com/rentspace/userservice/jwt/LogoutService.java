package com.rentspace.userservice.jwt;

import com.rentspace.userservice.exception.TokenNotFoundException;
import com.rentspace.userservice.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import static com.rentspace.userservice.util.TokenUtil.*;

@Component
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader(HEADER);

        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            String token = authHeader.substring(PREFIX_LENGTH);

            var changedtoken = tokenRepository.findByToken(token).orElseThrow(
                    () -> new TokenNotFoundException("Token : " + token + " not found")
            );
            changedtoken.setExpired(true);
            changedtoken.setRevoked(true);
            tokenRepository.save(changedtoken);

            SecurityContextHolder.clearContext();
        }
    }
}