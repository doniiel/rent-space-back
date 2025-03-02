package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.client.UserClient;
import com.rentspace.bookingservice.dto.PaymentTransactionDto;
import com.rentspace.bookingservice.entity.Booking;
import com.rentspace.bookingservice.entity.PaymentTransaction;
import com.rentspace.bookingservice.enums.PaymentStatus;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.exception.PaymentTransactionNotFound;
import com.rentspace.bookingservice.mapper.PaymentMapper;
import com.rentspace.bookingservice.repository.PaymentTransactionRepository;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.bookingservice.service.PaymentTransactionService;
import com.rentspace.core.event.PaymentCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BookingService bookingService;
    @Value("${event.topic.payment}")
    private String topicName;
    private final PaymentTransactionRepository repository;
    private final PaymentMapper mapper;
    private final UserClient userClient;

    @Override
    public PaymentTransactionDto createPaymentTransaction(Long bookingId, Long userId) {
        Booking booking = repository.findByBookingId(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Booking", "Id", bookingId));
        UserDto user = userClient.getUserById(userId);

        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .userId(userId)
                .booking(booking)
                .amount(booking.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .build();

        PaymentCreateEvent event = PaymentCreateEvent.builder()
                .paymentTransactionId(paymentTransaction.getId())
                .bookingId(booking.getId())
                .build();

        kafkaTemplate.send(topicName, event);
        return mapper.toDto(paymentTransaction);
    }

    @Override
    public PaymentTransactionDto setPaymentTransactionStatus(Long paymentId, PaymentStatus status) {
        PaymentTransaction existsPayment = repository.findById(paymentId).orElseThrow(
                () -> new PaymentTransactionNotFound("Payment", "Id", paymentId));

        existsPayment.setStatus(status);
        PaymentTransaction savedPayment = repository.save(existsPayment);
        return mapper.toDto(savedPayment);
    }
}
