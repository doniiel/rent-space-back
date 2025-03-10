package com.rentspace.paymentservice.service.impl;

import com.rentspace.core.dto.UserDto;
import com.rentspace.core.enums.PaymentStatus;
import com.rentspace.core.event.PaymentFailureEvent;
import com.rentspace.core.event.PaymentSuccessEvent;
import com.rentspace.core.exception.PaymentNotFoundException;
import com.rentspace.paymentservice.client.UserClient;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.entity.Payment;
import com.rentspace.paymentservice.mapper.PaymentMapper;
import com.rentspace.paymentservice.repository.PaymentRepository;
import com.rentspace.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final UserClient userClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${event.topic.payment.success}")
    private String paymentSuccessTopic;
    @Value("${event.topic.payment.failure}")
    private String paymentFailureTopic;

    @Override
    public PaymentDto createPayment(Long userId, Long bookingId, String currency, double amount) {
        var user = userClient.getUserById(userId).getBody();
        var payment = Payment.builder()
                .userId(user.getId())
                .bookingId(bookingId)
                .amount(amount)
                .currency(currency)
                .build();
        var savedPayment = repository.save(payment);
        return mapper.toDto(savedPayment);
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        var payment = repository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment", "paymentId", paymentId)
        );
        return mapper.toDto(payment);
    }

    @Override
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
    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status) {
        var payment = repository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment", "paymentId", paymentId)
        );
        payment.setStatus(status);
        var savedPayment = repository.save(payment);

        if (savedPayment.getBookingId() != null) {

            if (status == PaymentStatus.SUCCESS) {
                PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                        .bookingId(savedPayment.getBookingId())
                        .paymentId(savedPayment.getId())
                        .build();
                kafkaTemplate.send(paymentSuccessTopic, event).thenAccept(result ->
                        log.info("Sent payment success event for booking: {}", savedPayment.getBookingId()));
            } else {
                PaymentFailureEvent event = PaymentFailureEvent.builder()
                        .bookingId(savedPayment.getBookingId())
                        .paymentId(savedPayment.getId())
                        .build();
                kafkaTemplate.send(paymentFailureTopic, event).thenAccept(result ->
                        log.info("Sent payment failure event for booking: {}", savedPayment.getBookingId()));
            }
        } else {
            log.warn("Payment ID {} has no associated booking, skipping Kafka event.", savedPayment.getId());
        }

        return mapper.toDto(savedPayment);
    }
}