package com.rentspace.paymentservice.service.impl;

import com.rentspace.core.dto.UserDto;
import com.rentspace.core.enums.PaymentStatus;
import com.rentspace.core.event.PaymentFailureEvent;
import com.rentspace.core.event.PaymentSuccessEvent;
import com.rentspace.core.exception.PaymentNotFoundException;
import com.rentspace.paymentservice.client.UserClient;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.entity.Payment;
import com.rentspace.paymentservice.handler.PaymentPublisher;
import com.rentspace.paymentservice.mapper.PaymentMapper;
import com.rentspace.paymentservice.repository.PaymentRepository;
import com.rentspace.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final UserClient userClient;
    private final PaymentPublisher publisher;

    @Value("${event.topic.payment.success}")
    private String paymentSuccessTopic;

    @Value("${event.topic.payment.failure}")
    private String paymentFailureTopic;

    @Override
    @Transactional
    public PaymentDto createPayment(Long userId, Long bookingId, String currency, double amount) {
        validatePaymentInput(userId, bookingId, currency, amount);
        UserDto user = fetchUser(userId);
        Payment payment = buildPayment(user.getId(), bookingId, amount, currency);
        Payment savedPayment = repository.save(payment);
        return mapper.toDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = findPaymentById(paymentId);
        return mapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        List<Payment> payments = repository.findByUserId(userId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("Payment", "userId", userId);
        }
        return payments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = findPaymentById(paymentId);
        updatePayment(payment, status);
        publishPaymentEvent(payment);
        return mapper.toDto(payment);
    }

    private void validatePaymentInput(Long userId, Long bookingId, String currency, double amount) {
        ValidationUtils.validateNotNull(userId, "User ID");
        ValidationUtils.validateNotNull(bookingId, "Booking ID");
        ValidationUtils.validateNotNull(currency, "Currency");
        ValidationUtils.validatePositive(amount, "Amount");
    }

    private Payment savePayment(Payment payment) {
        Payment savedPayment = repository.save(payment);
        log.info("Saved payment with ID: {}", savedPayment.getId());
        return savedPayment;
    }

    private UserDto fetchUser(Long userId) {
        UserDto userDto = userClient.getUserById(userId).getBody();
        if (userDto == null) {
            throw new PaymentNotFoundException("User", "userId", userId);
        }
        return userDto;
    }

    private Payment findPaymentById(Long paymentId) {
        return repository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment", "paymentId", paymentId));
    }

    private void updatePayment(Payment payment, PaymentStatus status) {
        payment.setStatus(status);
        repository.save(payment);
        log.info("Updated payment status to {} for Payment ID: {}", status, payment.getId());
    }

    private Payment buildPayment(Long userId, Long bookingId, double amount, String currency) {
        return Payment.builder()
                .userId(userId)
                .bookingId(bookingId)
                .amount(amount)
                .currency(currency)
                .status(PaymentStatus.PENDING)
                .build();
    }

    private PaymentSuccessEvent createPaymentSuccessEvent(Long bookingId, Long paymentId) {
        return PaymentSuccessEvent.builder()
                .bookingId(bookingId)
                .paymentId(paymentId)
                .build();
    }

    private PaymentFailureEvent createPaymentFailureEvent(Long bookingId, Long paymentId) {
        return PaymentFailureEvent.builder()
                .bookingId(bookingId)
                .paymentId(paymentId)
                .build();
    }

    private void publishPaymentEvent(Payment payment) {
        if (payment.getBookingId() == null){
            log.warn("Payment ID {} has no associated booking, skipping event publication", payment.getId());
            return;
        }
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            PaymentSuccessEvent event = new PaymentSuccessEvent(payment.getBookingId(), payment.getId());
            publisher.publish(paymentSuccessTopic, payment.getId(), event, "payment success");
        } else if (payment.getStatus() == PaymentStatus.FAILURE) {
            PaymentFailureEvent event = new PaymentFailureEvent(payment.getBookingId(), payment.getId());
            publisher.publish(paymentFailureTopic, payment.getId(), event, "payment failure");
        }
    }
}