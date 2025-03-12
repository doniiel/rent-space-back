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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rentspace.core.util.ValidationUtils.validateDateRange;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final BookingPublisher publisher;

    @Value("${event.topic.listing.availability.request}")
    private String listingAvailabilityRequestTopic;

    @Value("${event.topic.listing-unblock}")
    private String listingUnblockTopic;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());
        Booking booking = buildBookingFromRequest(request);
        Booking savedBooking = saveBooking(booking);
        publishAvailabilityRequest(savedBooking);
        return mapper.toDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long bookingId) {
        return mapper.toDto(findBookingById(bookingId));
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = findBookingById(bookingId);
        deleteBooking(booking);
        publishUnblockEvent(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookingsByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public void updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = findBookingById(bookingId);
        booking.setStatus(status);
        repository.save(booking);
        log.info("Updated status to {} for booking ID: {}", status, bookingId);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        repository.deleteById(bookingId);
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

    private Booking saveBooking(Booking booking) {
        Booking savedBooking = repository.save(booking);
        log.info("Saved booking with ID: {}", savedBooking.getId());
        return savedBooking;
    }

    public Booking findBookingById(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking", "Id", bookingId));
    }

    private void deleteBooking(Booking booking) {
        repository.delete(booking);
        log.info("Deleted booking with ID: {}", booking.getId());
    }

    private void publishAvailabilityRequest(Booking booking) {
        ListingAvailabilityEvent event = createAvailabilityEvent(booking);
        publisher.publish(listingAvailabilityRequestTopic, booking.getId(), event, "availability request");
    }

    private void publishUnblockEvent(Booking booking) {
        ListingUnblockEvent event = createUnblockEvent(booking);
        publisher.publish(listingUnblockTopic, booking.getId(), event, "unblock request");
    }

    private ListingAvailabilityEvent createAvailabilityEvent(Booking booking) {
        return ListingAvailabilityEvent.builder()
                .bookingId(booking.getId())
                .listingId(booking.getListingId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
    }

    private ListingUnblockEvent createUnblockEvent(Booking booking) {
        return ListingUnblockEvent.builder()
                .bookingId(booking.getId())
                .listingId(booking.getListingId())
                .startDate(booking.getStartDate().toString())
                .endDate(booking.getEndDate().toString())
                .build();
    }
}