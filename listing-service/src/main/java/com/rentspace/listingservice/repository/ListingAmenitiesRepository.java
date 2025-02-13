package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAmenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingAmenitiesRepository extends JpaRepository<ListingAmenities, Long> {
    List<ListingAmenities> findByListingId(Long listingId);
}
