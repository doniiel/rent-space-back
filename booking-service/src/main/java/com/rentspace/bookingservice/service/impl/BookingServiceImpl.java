package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.client.ListingClient;
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
    private final ListingClient listingClient;
    @Value("${event.topic.listing}")
    private String listingTopic;
    @Value("${event.topic.listing-unblock}")
    private String listingUnblockTopic;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {
        log.info("Sending availability check event for listingId {}, startDate {}, endDate {}",
                request.getListingId(), request.getStartDate(), request.getEndDate());
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(PENDING)
                .build();

        Booking savedBooking = repository.save(booking);

        ListingAvailabilityEvent event = ListingAvailabilityEvent.builder()
                .bookingId(booking.getId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();


        kafkaTemplate.send(listingTopic, event);
        log.info("Sent listing availability event for booking ID: {}", savedBooking.getId());

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
}
