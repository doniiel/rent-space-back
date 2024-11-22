package com.rentspace.listingservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(CONFLICT)
public class ListingAlreadyExistsException extends RuntimeException{
    public ListingAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
