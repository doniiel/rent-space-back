package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Listings;
import com.rentspace.listingservice.entity.Photos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotosRepository extends JpaRepository<Photos, Long> {
    List<Photos> findByListings_Id(Long id);
    Optional<Photos> findByListings_IdAndUrl(Long id, String url);
}
