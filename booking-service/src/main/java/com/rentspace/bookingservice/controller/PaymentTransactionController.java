package com.rentspace.bookingservice.controller;

import com.rentspace.bookingservice.dto.PaymentTransactionDto;
import com.rentspace.bookingservice.service.PaymentTransactionService;
import com.rentspace.core.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentTransactionController {
    private final PaymentTransactionService paymentTransactionService;

    @PostMapping
    public ResponseEntity<PaymentTransactionDto> createPaymentTransaction(
            @RequestParam Long userId,
            @RequestParam Long bookingId) {
        return ResponseEntity
                .status(CREATED)
                .body(paymentTransactionService.createPaymentTransaction(userId, bookingId));
    }

    @PutMapping("/{paymentId}/status/{status}")
    public ResponseEntity<PaymentTransactionDto> updatePaymentStatus(
            @PathVariable Long paymentId,
            @PathVariable PaymentStatus status) {
        return ResponseEntity
                .status(OK)
                .body(paymentTransactionService.setPaymentTransactionStatus(paymentId,status));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentTransactionDto>> getPaymentByUserId(@PathVariable Long userId) {
        return ResponseEntity
                .status(OK)
                .body(paymentTransactionService.getPaymentByUserId(userId));
    }

}
