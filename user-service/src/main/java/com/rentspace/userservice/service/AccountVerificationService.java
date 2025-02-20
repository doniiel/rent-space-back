package com.rentspace.userservice.service;

import com.rentspace.userservice.entity.user.User;

public interface AccountVerificationService {
    void sendVerificationEmail(User user);
    void confirmAccount(String token);
}
