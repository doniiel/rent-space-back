package com.rentspace.search_service.exception;

public class SearchServiceException extends RuntimeException {
    public SearchServiceException(String message) {
        super(message);
    }
    public SearchServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
