package com.rentspace.bookingservice.service;

import com.rentspace.bookingservice.dto.PaymentTransactionDto;
import com.rentspace.bookingservice.enums.PaymentStatus;

public interface PaymentTransactionService {
    PaymentTransactionDto createPaymentTransaction(Long bookingId, Long userId);
    PaymentTransactionDto setPaymentTransactionStatus(Long bookingId, PaymentStatus status);
}