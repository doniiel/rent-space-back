package com.rentspace.search_service.mapper;

import com.rentspace.core.event.ListingEvent;
import com.rentspace.search_service.model.Listing;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListingMapper {
    Listing toEntity(ListingEvent event);
}
