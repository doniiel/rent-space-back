package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;
import com.rentspace.bookingservice.entity.Booking;
import com.rentspace.bookingservice.enums.BookingStatus;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.handler.BookingPublisher;
import com.rentspace.bookingservice.mapper.BookingMapper;
import com.rentspace.bookingservice.repository.BookingRepository;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.core.event.ListingAvailabilityEvent;
import com.rentspace.core.event.ListingUnblockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.rentspace.core.util.ValidationUtils.validateDateRange;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final BookingPublisher publisher;

    @Value("${event.topic.listing.availability.request}")
    private String listingAvailabilityRequestTopic;

    @Value("${event.topic.listing.availability.unblock}")
    private String listingUnblockTopic;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {
        Objects.requireNonNull(request, "Booking request cannot be null");
        validateDateRange(request.getStartDate(), request.getEndDate());
        Booking booking = buildBookingFromRequest(request);
        Booking savedBooking = repository.save(booking);
        publishAvailabilityRequest(savedBooking);
        log.info("Booking created with ID: {}", savedBooking.getId());
        return mapper.toDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long bookingId) {
        Objects.requireNonNull(bookingId, "Booking ID cannot be null");
        return mapper.toDto(findBookingById(bookingId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> getAllBookingsByUserId(Long userId, Pageable pageable) {
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(pageable, "Pageable cannot be null");
        return repository.findByUserId(userId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> getAllBookings(Pageable pageable) {
        Objects.requireNonNull(pageable, "Pageable cannot be null");
        return repository.findAll(pageable).map(mapper::toDto);
    }


    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Objects.requireNonNull(bookingId, "Booking ID cannot be null");
        Booking booking = findBookingById(bookingId);
        repository.delete(booking);
        publishUnblockEvent(booking);
        log.info("Booking cancelled with ID: {}", bookingId);
    }

    @Override
    @Transactional
    public void updateBookingStatus(Long bookingId, BookingStatus status) {
        Objects.requireNonNull(bookingId, "Booking ID cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
        Booking booking = findBookingById(bookingId);
        booking.setStatus(status);
        repository.save(booking);
        log.info("Updated status to {} for booking ID: {}", status, bookingId);
    }

    private Booking buildBookingFromRequest(CreateBookingRequest request) {
        return Booking.builder()
                .userId(request.getUserId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(request.getTotalPrice())
                .status(BookingStatus.PENDING)
                .build();
    }


    private Booking findBookingById(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking", "ID", bookingId));
    }

    private void publishAvailabilityRequest(Booking booking) {
        ListingAvailabilityEvent event = ListingAvailabilityEvent.builder()
                .bookingId(booking.getId())
                .listingId(booking.getListingId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
        publisher.publish(listingAvailabilityRequestTopic, booking.getId(), event, "availability request");
    }

    private void publishUnblockEvent(Booking booking) {
        ListingUnblockEvent event = ListingUnblockEvent.builder()
                .bookingId(booking.getId())
                .listingId(booking.getListingId())
                .startDate(booking.getStartDate().toString())
                .endDate(booking.getEndDate().toString())
                .build();
        publisher.publish(listingUnblockTopic, booking.getId(), event, "unblock request");
    }
}