package com.rentspace.search_service.repository;

import com.rentspace.search_service.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends ElasticsearchRepository<Listing, Long> {
    List<Listing> findByTitleContainingOrDescriptionContainingOrCityContaining(String title, String description, String city);
    List<Listing> findByCityAndTypeAndPricePerNightLessThanEqual(String city, String type, Double maxPrice);
    Page<Listing> findByFilters(String city, String type, Double minPrice, Double maxPrice, Integer minGuests, List<String> amenities, Pageable pageable);
    List<Listing> findByCity(String city);
}
