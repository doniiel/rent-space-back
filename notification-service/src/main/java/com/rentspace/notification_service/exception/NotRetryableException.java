package com.rentspace.notification_service.exception;

public class NotRetryableException extends RuntimeException {

    public NotRetryableException(Exception exception) {super(exception);}
    public NotRetryableException(String message) {
        super(message);
    }
}
