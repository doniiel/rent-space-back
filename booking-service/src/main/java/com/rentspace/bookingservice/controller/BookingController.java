package com.rentspace.bookingservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @PostMapping
    public ResponseEntity<?> createBooking() {

    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBookingsByUserId() {

    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking() {

    }

    @PostMapping("/payments")
    public ResponseEntity<?> createPayment() {

    }

    @GetMapping("/payments/{userId}")
    public ResponseEntity<?> getPaymentsByUserId() {

    }


}
