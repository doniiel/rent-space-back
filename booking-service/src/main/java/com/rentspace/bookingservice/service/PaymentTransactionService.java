package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.PaymentTransactionDto;
import com.rentspace.core.enums.PaymentStatus;

import java.util.List;

public interface PaymentTransactionService {
    PaymentTransactionDto createPaymentTransaction(Long bookingId, Long userId);
    PaymentTransactionDto setPaymentTransactionStatus(Long bookingId, PaymentStatus status);
    List<PaymentTransactionDto> getPaymentByUserId(Long userId);
}