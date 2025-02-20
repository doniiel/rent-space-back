package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingRequestDto bookingRequestDto);
    BookingDto confirmBooking(Long bookingId);
    void cancelBooking(Long bookingId);
    BookingDto getBookingById(Long bookingId);
    List<BookingDto> getBookingsByUserId(Long userId);
}
