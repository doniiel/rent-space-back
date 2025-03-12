package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;
import com.rentspace.bookingservice.entity.Booking;
import com.rentspace.bookingservice.enums.BookingStatus;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.mapper.BookingMapper;
import com.rentspace.bookingservice.repository.BookingRepository;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.core.event.ListingAvailabilityEvent;
import com.rentspace.core.event.ListingUnblockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.rentspace.bookingservice.enums.BookingStatus.PENDING;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${event.topic.listing.availability.request}")
    private String listingTopic;
    @Value("${event.topic.listing-unblock}")
    private String listingUnblockTopic;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {
        log.info("Creating booking for listingId {}, startDate {}, endDate {}",
                request.getListingId(), request.getStartDate(), request.getEndDate());

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(request.getTotalPrice())
                .status(PENDING)
                .build();

        Booking savedBooking = repository.save(booking);

        ListingAvailabilityEvent event = ListingAvailabilityEvent.builder()
                .bookingId(savedBooking.getId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        sendListingAvailabilityEvent(savedBooking, event);
        return mapper.toDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long bookingId) {
        Booking existBooking = repository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));
        return mapper.toDto(existBooking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        log.info("Cancelling booking ID: {}", bookingId);

        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));

        repository.deleteById(bookingId);
        log.info("Booking ID: {} deleted successfully", bookingId);

        ListingUnblockEvent event = ListingUnblockEvent.builder()
                .bookingId(bookingId)
                .listingId(booking.getListingId())
                .startDate(booking.getStartDate().toString())
                .endDate(booking.getEndDate().toString())
                .build();

        kafkaTemplate.send(listingUnblockTopic, event);
        log.info("Sent listing unblock event for booking ID: {}", bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookingsByUserId(Long userId) {
        List<Booking> bookings = repository.findByUserId(userId);
        return bookings.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking existBooking = repository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));
        existBooking.setStatus(status);
        repository.save(existBooking);
        log.info("Updated booking status for booking ID: {}", bookingId);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        repository.deleteById(bookingId);
    }

    private void sendListingAvailabilityEvent(Booking booking, ListingAvailabilityEvent event) {
        try {
            kafkaTemplate.send(listingTopic, booking.getId().toString(), event);
            log.info("Sent listing availability event for booking ID: {}", booking.getId());
        } catch (Exception e) {
            log.error("Failed to send Kafka event for booking ID: {}", booking.getId());
            repository.delete(booking);
            throw e;
        }
    }
}
