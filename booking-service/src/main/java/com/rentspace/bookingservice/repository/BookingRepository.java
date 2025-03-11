package com.rentspace.bookingservice.repository;

import com.rentspace.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Query("""
            SELECT COUNT(b) > 0 
                        FROM Booking b
                                    WHERE b.listingId = :listingId
                                                AND b.status NOT IN 
            """)
    boolean existsOverlappingBooking(@Param("listingId") Long listingId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate")   LocalDateTime endDate);
}
