package com.rentspace.userservice.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {
    void sendResetCode(String email);
    void resetPassword(String email, String resetCode, String newPassword);
}