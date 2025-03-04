package com.rentspace.bookingservice.service.impl;

import com.rentspace.bookingservice.client.UserClient;
import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.PaymentTransactionDto;
import com.rentspace.bookingservice.entity.PaymentTransaction;
import com.rentspace.bookingservice.exception.PaymentTransactionNotFound;
import com.rentspace.bookingservice.mapper.BookingMapper;
import com.rentspace.bookingservice.mapper.PaymentMapper;
import com.rentspace.bookingservice.repository.PaymentTransactionRepository;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.bookingservice.service.PaymentTransactionService;
import com.rentspace.core.dto.UserDto;
import com.rentspace.core.enums.PaymentStatus;
import com.rentspace.core.event.PaymentCreateEvent;
import com.rentspace.core.exception.UserNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
    private final PaymentTransactionRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final UserClient userClient;
    private final PaymentMapper mapper;
    @Value("${event.topic.payment}")
    private String topicName;

    @Override
    @Transactional
    public PaymentTransactionDto createPaymentTransaction(Long bookingId, Long userId) {
        BookingDto bookingDto = bookingService.getBookingById(bookingId);
        UserDto user;
        try {
            user = userClient.getUserById(userId);
        } catch (FeignException.NotFound ex) {
            throw new UserNotFoundException("User", "Id", userId);
        }

        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .userId(user.getId())
                .booking(bookingMapper.toEntity(bookingDto))
                .amount(bookingDto.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .build();

        PaymentTransaction savedTransaction = repository.save(paymentTransaction);

        PaymentCreateEvent event = PaymentCreateEvent.builder()
                .paymentTransactionId(savedTransaction.getId())
                .bookingId(bookingDto.getId())
                .build();

        kafkaTemplate.send(topicName, event);
        return mapper.toDto(savedTransaction);
    }

    @Override
    @Transactional
    public PaymentTransactionDto setPaymentTransactionStatus(Long paymentId, PaymentStatus status) {
        PaymentTransaction existsPayment = repository.findById(paymentId).orElseThrow(
                () -> new PaymentTransactionNotFound("Payment", "Id", paymentId));

        log.info("Updating payment status for Payment ID: {}, new status: {}", paymentId, status);
        existsPayment.setStatus(status);
        PaymentTransaction savedPayment = repository.save(existsPayment);
        return mapper.toDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentTransactionDto> getPaymentByUserId(Long userId) {
        List<PaymentTransaction> payments = repository.findByUserId(userId);
        return payments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
