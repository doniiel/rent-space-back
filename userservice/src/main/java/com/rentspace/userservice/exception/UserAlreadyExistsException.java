package com.rentspace.userservice.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String entity, String field, Object value) {
        super(String.format("%s with %s '%s' already exists.", entity, field, value));
    }
}
