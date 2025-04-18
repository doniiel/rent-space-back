package com.rentspace.search_service.repository;

import com.rentspace.search_service.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends ElasticsearchRepository<Listing, Long> {
    List<Listing> findByTitleContainingOrDescriptionContainingOrCityContaining(
            String title, String description, String city);

    @Query("{\"bool\": {" +
            "\"filter\": [" +
            "  {\"match\": {\"city\": \"?0\"}}," +
            "  {\"match\": {\"type\": \"?1\"}}," +
            "  {\"range\": {\"pricePerNight\": {\"gte\": \"?2\", \"lte\": \"?3\"}}}," +
            "  {\"match\": {\"maxGuests\": \"?4\"}}" +
            "]}}")
    Page<Listing> findByFilters(String city, String type, Double priceMin, Double priceMax, Integer maxGuests, Pageable pageable);
}
