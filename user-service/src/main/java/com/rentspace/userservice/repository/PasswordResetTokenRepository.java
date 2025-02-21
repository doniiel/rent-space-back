package com.rentspace.userservice.repository;

import com.rentspace.userservice.entity.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenAndUser_Email(String token, String userEmail);
    void deleteByUser_Email(String userEmail);
}
