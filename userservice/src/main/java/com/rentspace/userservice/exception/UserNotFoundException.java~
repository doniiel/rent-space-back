package com.rentspace.userservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String resourceName, String fieldName, Object fieldValue) {

        super(format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
