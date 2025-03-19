package com.rentspace.bookingservice.controller;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.dto.CreateBookingRequest;
import com.rentspace.bookingservice.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
@Tag(name = "Bookings REST API in RentSpace", description = "APIs to manage bookings")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Create a new booking", description = "Creates a new booking based on the provided request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Booking created successfully", content = @Content(schema = @Schema(implementation = BookingDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking data"),
            @ApiResponse(responseCode = "409", description = "Booking conflict (e.g., listing unavailable)")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {
        BookingDto createdBooking = bookingService.createBooking(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBooking.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdBooking);
    }

    @Operation(summary = "Get booking by ID", description = "Retrieves a booking by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully", content = @Content(schema = @Schema(implementation = BookingDto.class))),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(
            @PathVariable @Min(1) Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @Operation(summary = "Get bookings by user ID", description = "Retrieves a paginated list of bookings for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID")
    })
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookingDto>> getBookingsByUserId(
            @PathVariable @Min(1) @Parameter(description = "User ID", example = "1") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookingService.getAllBookingsByUserId(userId, pageable));
    }

    @Operation(summary = "Get all bookings", description = "Retrieves a paginated list of all bookings (admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<BookingDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookingService.getAllBookings(pageable));
    }

    @Operation(summary = "Cancel a booking", description = "Cancels an existing booking by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Booking cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable @Min(1) Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}