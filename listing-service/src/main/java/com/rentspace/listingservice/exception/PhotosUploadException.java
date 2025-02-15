package com.rentspace.listingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PhotosUploadException extends RuntimeException {
    public PhotosUploadException(String message) {
        super(message);
    }
}
