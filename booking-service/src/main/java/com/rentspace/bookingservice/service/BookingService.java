package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.BookingRequestDto;
import com.rentspace.bookingservice.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);
    BookingResponseDto cancelBooking(Long bookingId);
    BookingResponseDto confirmBooking(Long bookingId);
    List<BookingResponseDto> getBookingsByUserId(Long userId);
    List<BookingResponseDto> getBookingsByListingId(Long listingId);
}
