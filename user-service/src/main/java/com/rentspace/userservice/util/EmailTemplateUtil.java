package com.rentspace.userservice.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateUtil {
    @Getter
    @Value("${email.verification.subject}")
    private String verificationSubject;

    @Getter
    @Value("${email.password-reset.subject}")
    private String passwordResetSubject;

    @Value("${email.verification.message}")
    private String verificationMessage;

    @Value("${email.password-reset.message}")
    private String passwordResetMessage;

    public String getVerificationMessage(String link) {
        return String.format(verificationMessage, link);
    }

    public String getPasswordResetMessage(String resetCode) {
        return String.format(passwordResetMessage, resetCode);
    }
}
