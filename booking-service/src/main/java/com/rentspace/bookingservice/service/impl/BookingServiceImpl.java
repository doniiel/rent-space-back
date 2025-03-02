package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;
import com.rentspace.bookingservice.entity.Booking;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.mapper.BookingMapper;
import com.rentspace.bookingservice.repository.BookingRepository;
import com.rentspace.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rentspace.bookingservice.enums.BookingStatus.CANCELLED;
import static com.rentspace.bookingservice.enums.BookingStatus.PENDING;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final BookingMapper mapper;

    @Override
    public BookingDto createBooking(CreateBookingRequest request) {
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(PENDING)
                .build();
        Booking savedBooking = repository.save(booking);
        return mapper.toDto(savedBooking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId) {
        Booking existBooking = repository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));
        return mapper.toDto(existBooking);
    }

    @Override
    public BookingDto cancelBooking(Long bookingId) {
        Booking existBooking = repository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));
        existBooking.setStatus(CANCELLED);
        Booking savedBooking = repository.save(existBooking);
        return mapper.toDto(savedBooking);
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId) {
        List<Booking> bookings = repository.findByUserId(userId);
        return bookings.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
