package com.rentspace.search_service.repository;

import com.rentspace.search_service.model.Listing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends ElasticsearchRepository<Listing, Long> {
}
