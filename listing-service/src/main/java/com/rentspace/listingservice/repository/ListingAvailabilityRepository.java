package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ListingAvailabilityRepository extends JpaRepository<ListingAvailability, Long> {
    List<ListingAvailability> findByListingId(Long listingId);
    boolean existsByListingIdAndAvailableTrueAndStartDateBeforeAndEndDateAfter(
            Long listingId, LocalDate endDate, LocalDate startDate);

    int deleteByListingIdAndStartDateAndEndDateAndAvailableFalse(
            Long listingId, LocalDate startDate, LocalDate endDate);

}
