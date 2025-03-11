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
import com.rentspace.core.event.BookingCreatedEvent;
import com.rentspace.core.exception.ListingNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ListingClient listingClient;
    @Value("${event.topic.payment}")
    private String paymentTopic;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request) {
        log.info("Checking availability for listing ID: {} from {} to {}",
                request.getListingId(), request.getStartDate(), request.getEndDate());
        Boolean isAvailable = listingClient.checkAvailability(
                request.getListingId(),
                request.getStartDate().toString(),
                request.getEndDate().toString()
        );

        if (Boolean.FALSE.equals(isAvailable)) {
            throw new ListingNotAvailableException(
                    request.getListingId(),
                    request.getStartDate().toString(),
                    request.getEndDate().toString()
            );
        }

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .listingId(request.getListingId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(PENDING)
                .build();

        log.info("Creating booking for User ID: {}, Listing ID: {}", request.getUserId(), request.getListingId());
        Booking savedBooking = repository.save(booking);

        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingId(savedBooking.getId())
                .listingId(savedBooking.getListingId())
                .userId(savedBooking.getUserId())
                .totalPrice(savedBooking.getTotalPrice())
                .build();

        log.info("Sending payment event for booking ID: {}", savedBooking.getId());
        kafkaTemplate.send(paymentTopic, event).thenAccept(result ->
                log.info("Payment event sent for booking ID: {}", savedBooking.getId())
        );

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
    public BookingDto cancelBooking(Long bookingId) {
        Booking existBooking = repository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));
        existBooking.setStatus(CANCELLED);
        Booking savedBooking = repository.save(existBooking);
        return mapper.toDto(savedBooking);
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
}
