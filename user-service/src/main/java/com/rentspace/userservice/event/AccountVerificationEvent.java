package com.rentspace.userservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountVerificationEvent {
    private String email;
    private String subject;
    private String message;
}
