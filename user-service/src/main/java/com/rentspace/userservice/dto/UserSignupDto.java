package com.rentspace.userservice.dto;

import lombok.Data;

@Data
public class UserSignupDto {
    private String username;
    private String password;
    private String email;
}
