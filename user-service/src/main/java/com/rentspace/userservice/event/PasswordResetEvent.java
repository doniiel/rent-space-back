package com.rentspace.userservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetEvent {
    private String email;
    private String subject;
    private String message;
}
