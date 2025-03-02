package com.rentspace.bookingservice.dto;

import com.rentspace.bookingservice.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    private Long listingId;
    private Long userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
