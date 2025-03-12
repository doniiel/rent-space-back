package com.rentspace.paymentservice.controller;

import com.rentspace.paymentservice.dto.PaymentCreateRequest;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.enums.PaymentStatus;
import com.rentspace.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final RestartEndpoint.PauseEndpoint pauseEndpoint;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentDto> createPayment(@Valid PaymentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                paymentService.createPayment(request.getUserId(), request.getBookingId(), request.getCurrency(), request.getAmount())
        );
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDto> updateStatus(@PathVariable Long paymentId,
                                                   @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, status));
    }
}
