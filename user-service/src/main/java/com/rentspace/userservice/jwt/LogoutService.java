package com.rentspace.userservice.jwt;

import com.rentspace.userservice.entity.token.Token;
import com.rentspace.userservice.exception.TokenNotFoundException;
import com.rentspace.userservice.repository.TokenRepository;
import com.rentspace.userservice.util.TokenUtil;
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
    private final TokenUtil tokenUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
       String token = tokenUtil.extractTokenFromHeader(request.getHeader(HEADER));

        if (token != null ) {
            Token storedToken = tokenRepository.findByToken(token).orElseThrow(
                    () -> new TokenNotFoundException("Token : " + token + " not found"));
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}