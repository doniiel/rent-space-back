package com.rentspace.bookingservice.repository;

import com.rentspace.bookingservice.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByListingId(Long listingId);
    Page<Booking> findByUserId(Long userId, Pageable pageable);
    Page<Booking> findAll(Pageable pageable);
    Page<Booking> findByListingId(Long listingId, Pageable pageable);
}
