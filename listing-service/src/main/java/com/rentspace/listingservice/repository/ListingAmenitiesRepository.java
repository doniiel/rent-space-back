package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAmenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingAmenitiesRepository extends JpaRepository<ListingAmenities, Long> {
}
