package com.rentspace.paymentservice.service;

import com.rentspace.core.enums.PaymentStatus;
import com.rentspace.paymentservice.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(Long userId, Long bookingId, String currency, double amount);
    PaymentDto getPaymentById(Long paymentId);
    List<PaymentDto> getPaymentsByUserId(Long userId);
    PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status);
}
