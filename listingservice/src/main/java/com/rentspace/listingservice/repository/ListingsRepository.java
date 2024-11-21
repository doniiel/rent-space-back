package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Listings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingsRepository extends JpaRepository<Listings, Long> {
}
