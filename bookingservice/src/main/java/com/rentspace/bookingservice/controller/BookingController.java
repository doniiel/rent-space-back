package com.rentspace.bookingservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    @PostMapping
    public String createBooking(@RequestBody String bookingDetails) {
        // Логика для создания бронирования
        return "Booking created successfully!";
    }
}
