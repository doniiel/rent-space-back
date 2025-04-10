package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAmenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListingAmenitiesRepository extends JpaRepository<ListingAmenities, Long> {
    @Query("SELECT la FROM ListingAmenities la LEFT JOIN FETCH la.listing WHERE la.listing.id = :listingId")
    Optional<ListingAmenities> findByListingId(@Param("listingId") Long listingId);
}
