package com.rentspace.listingservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(NOT_FOUND)
public class ListingNotFoundException extends RuntimeException{
        public ListingNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        }
        public ListingNotFoundException(String message) {
            super(message);
        }
}
