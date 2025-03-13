package com.rentspace.paymentservice.controller;

import com.rentspace.paymentservice.dto.PaymentCreateRequest;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.dto.PaymentStatusUpdateRequest;
import com.rentspace.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Get Payment by ID", description = "Retrieve a payment by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    }
    )
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @Operation(summary = "Get all payments for a user", description = "Retrieve all payments associated with a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No payments found for the user")
    })
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getPaymentsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    @Operation(summary = "Create a new payment", description = "Create a payment with provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details")
    })
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                paymentService.createPayment(request.getUserId(),
                                             request.getBookingId(),
                                             request.getCurrency(),
                                             request.getAmount()));
    }

    @Operation(summary = "Update payment status", description = "Update the status of a existing payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid payment status")
    })
    @PatchMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> updatePaymentStatus(@PathVariable Long paymentId,
                                                   @RequestBody PaymentStatusUpdateRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, request.getStatus()));
    }
}
