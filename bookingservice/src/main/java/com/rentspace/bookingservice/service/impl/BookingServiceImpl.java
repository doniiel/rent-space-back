package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.client.ListingClient;
import com.rentspace.bookingservice.client.UserClient;
import com.rentspace.bookingservice.dto.BookingRequestDto;
import com.rentspace.bookingservice.dto.BookingResponseDto;
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
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        return null;
    }

    @Override
    public BookingResponseDto cancelBooking(Long bookingId) {
        return null;
    }

    @Override
    public BookingResponseDto confirmBooking(Long bookingId) {
        return null;
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(Long userId) {
        return null;
    }

    @Override
    public List<BookingResponseDto> getBookingsByListingId(Long listingId) {
        return null;
    }
}
