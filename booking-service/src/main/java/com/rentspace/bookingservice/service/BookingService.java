package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;
import com.rentspace.bookingservice.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingDto createBooking(CreateBookingRequest request);
    BookingDto getBookingById(Long bookingId);
    Page<BookingDto> getAllBookingsByUserId(Long userId, Pageable pageable);
    Page<BookingDto> getAllBookingsByListingId(Long listingId, Pageable pageable);
    Page<BookingDto> getAllBookings(Pageable pageable);
    void cancelBooking(Long bookingId);
    void updateBookingStatus(Long bookingId, BookingStatus status);
}
