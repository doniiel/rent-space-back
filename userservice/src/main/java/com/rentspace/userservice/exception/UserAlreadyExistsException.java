package com.rentspace.userservice.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(format("%s already exists with given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
