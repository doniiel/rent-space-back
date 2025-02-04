package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.RegisterRequest;

import java.util.Map;

public interface AuthService {

    Map<String, String> login(String username, String password); // DONE
    Map<String, Object> register(RegisterRequest request); // DONE
    Map<String, String> refresh(String token); // DONE

}
