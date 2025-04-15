package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ListingAvailabilityRepository extends JpaRepository<ListingAvailability, Long> {
    List<ListingAvailability> findByListingId(Long listingId);

    @Query("SELECT CASE WHEN COUNT(la) > 0 THEN true ELSE false END " +
            "FROM ListingAvailability la " +
            "WHERE la.listing.id = :listingId " +
            "AND la.startDate <= :endDate " +
            "AND la.endDate >= :startDate")
    boolean existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long listingId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT CASE WHEN COUNT(la) > 0 THEN true ELSE false END " +
            "FROM ListingAvailability la " +
            "WHERE la.listing.id = :listingId " +
            "AND la.startDate <= :endDate " +
            "AND la.endDate >= :startDate " +
            "AND la.id != :excludeId")
    boolean existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndIdNot(
            Long listingId, LocalDateTime startDate, LocalDateTime endDate, Long excludeId);

    int deleteByListingIdAndStartDateAndEndDateAndAvailableFalse(
            Long listingId, LocalDateTime startDate, LocalDateTime endDate);

    Optional<ListingAvailability> findByListingIdAndStartDateAndEndDate(
            Long listingId, LocalDateTime startDate, LocalDateTime endDate);
}
