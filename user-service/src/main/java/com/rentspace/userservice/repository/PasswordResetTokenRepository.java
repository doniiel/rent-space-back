package com.rentspace.userservice.repository;

import com.rentspace.userservice.entity.token.PasswordResetToken;
import com.rentspace.userservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenAndUser_Email(String token, String userEmail);
    Optional<PasswordResetToken> findByUser(User user);
}
