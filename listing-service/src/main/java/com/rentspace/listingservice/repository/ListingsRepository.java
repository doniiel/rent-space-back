package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingsRepository extends JpaRepository<Listing, Long> {
    boolean existsById(Long id);
    List<Listing> findByUserId(Long userId);
    @Query("SELECT l FROM Listing l LEFT JOIN FETCH l.photos WHERE l.id = :id")
    Optional<Listing> findByIdWithPhotos(@Param("id") Long id);
}
