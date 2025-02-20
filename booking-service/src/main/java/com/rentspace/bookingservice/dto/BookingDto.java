package com.rentspace.bookingservice.dto;

import com.rentspace.bookingservice.util.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;
    private Long userId;
    private Long listingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private Status status;
}
