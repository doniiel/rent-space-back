package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.service.PasswordResetService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Override
    public String generateResetCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    @Override
    public void sendResetCode(String email, String resetCode) {
        System.out.println("Отправка кода сброса на email: " + email + " Код: " + resetCode);
    }

    @Override
    public boolean validateResetCode(String email, String resetCode) {
        return true;
    }
}
