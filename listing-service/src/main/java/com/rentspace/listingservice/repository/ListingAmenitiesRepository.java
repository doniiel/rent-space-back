package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAmenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingAmenitiesRepository extends JpaRepository<ListingAmenities, Long> {
    Optional<ListingAmenities> findByListingId(Long listingId);
}
