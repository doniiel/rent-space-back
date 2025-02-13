package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.entity.ListingAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListingAvailabilityMapper {
    ListingAvailability toEntity(ListingAvailabilityDto dto);
    @Mapping(source = "listing.id", target = "listingId")
    ListingAvailabilityDto toDto(ListingAvailability entity);
}
