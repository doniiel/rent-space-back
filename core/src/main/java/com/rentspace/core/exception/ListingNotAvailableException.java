package com.rentspace.core.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ListingNotAvailableException extends RuntimeException {
    public ListingNotAvailableException(Long listingId, String startDate, String endDate) {
        super("Listing with ID " + listingId + " is not available from" + startDate + " to " + endDate);
    }
}
