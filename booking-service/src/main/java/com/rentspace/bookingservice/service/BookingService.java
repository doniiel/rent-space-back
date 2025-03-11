package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;
import com.rentspace.bookingservice.enums.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingRequest createBookingRequest);
    BookingDto getBookingById(Long bookingId);
    void cancelBooking(Long bookingId);
    List<BookingDto> getAllBookingsByUserId(Long userId);
    void updateBookingStatus(Long bookingId, BookingStatus status);
    void deleteBooking(Long bookingId);
}
