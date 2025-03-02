package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingRequest createBookingRequest);
    BookingDto getBookingById(Long bookingId);
    BookingDto cancelBooking(Long bookingId);
    List<BookingDto> getAllBookingsByUserId(Long userId);
}
