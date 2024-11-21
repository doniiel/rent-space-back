package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Photos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotosRepository extends JpaRepository<Photos, Long> {
}
