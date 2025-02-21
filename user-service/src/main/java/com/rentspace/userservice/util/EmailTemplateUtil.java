package com.rentspace.userservice.util;

public class EmailTemplateUtil {
    public static final String VERIFICATION_SUBJECT = "Account Verification";
    public static final String VERIFICATION_MESSAGE = "Please click the link to verify your account: ";
    public static final String PASSWORD_RESET_SUBJECT = "Password Reset Request";

    public static String getVerificationMessage(String link) {
        return "Please click the link to verify your account: " + link;
    }

    public static String getPasswordResetMessage(String resetCode) {
        return "Your password reset code: " + resetCode;
    }
}
