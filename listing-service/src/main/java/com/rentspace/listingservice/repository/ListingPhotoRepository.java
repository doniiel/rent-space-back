package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingPhotoRepository extends JpaRepository<ListingPhoto, Long> {
    List<ListingPhoto> findByListing(Listing listing);
    List<ListingPhoto> findByListingId(Long listingId);

    void deleteByListing(Listing listing);
}
