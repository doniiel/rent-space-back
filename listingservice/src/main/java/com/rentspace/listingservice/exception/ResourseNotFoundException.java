package com.rentspace.listingservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(NOT_FOUND)
public class ResourseNotFoundException extends RuntimeException{
    public ResourseNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
