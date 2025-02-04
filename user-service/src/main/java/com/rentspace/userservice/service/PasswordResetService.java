package com.rentspace.userservice.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {

    // Генерация случайного кода для сброса пароля
    public String generateResetCode();

    // Отправка кода сброса пароля на email пользователя
    public void sendResetCode(String email, String resetCode);

    // Проверка правильности кода сброса
    public boolean validateResetCode(String email, String resetCode);
}