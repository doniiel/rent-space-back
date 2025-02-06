package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.ListingAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingAvailabilityRepository extends JpaRepository<ListingAvailability, Long> {
}
