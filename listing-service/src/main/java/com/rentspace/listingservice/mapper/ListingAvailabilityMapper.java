package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.entity.ListingAvailability;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListingAvailabilityMapper {
    ListingAvailability toEntity(ListingAvailabilityDto dto);
    ListingAvailabilityDto toDto(ListingAvailability entity);
}
