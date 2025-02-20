package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.repository.BookingRepository;
import com.rentspace.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    @Override
    public BookingDto createBooking(BookingRequestDto bookingRequestDto) {
        return null;
    }

    @Override
    public BookingDto cancelBooking(Long bookingId) {
        return null;
    }

    @Override
    public BookingDto confirmBooking(Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByUserId(Long userId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByListingId(Long listingId) {
        return null;
    }
}
