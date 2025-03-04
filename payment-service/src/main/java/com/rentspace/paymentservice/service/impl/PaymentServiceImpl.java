package com.rentspace.paymentservice.service.impl;

import com.rentspace.core.dto.UserDto;
import com.rentspace.core.enums.PaymentStatus;
import com.rentspace.paymentservice.client.UserClient;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.entity.Payment;
import com.rentspace.paymentservice.mapper.PaymentMapper;
import com.rentspace.paymentservice.repository.PaymentRepository;
import com.rentspace.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserClient userClient;


    @Override
    public PaymentDto createPayment(Long userId, String currency, double amount) {
        UserDto user = userClient.getUserById(userId);
        Payment payment = Payment.builder()
                .userId(user.getId())
                .amount(amount)
                .currency(Currency.getInstance(currency))
                .status(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {

    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(Long userId) {

    }

    @Override
    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status) {

    }
}