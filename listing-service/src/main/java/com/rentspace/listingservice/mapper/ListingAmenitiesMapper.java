package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.entity.ListingAmenities;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListingAmenitiesMapper {
    @Mapping(source = "listingId", target = "listing.id")
    ListingAmenities toEntity(ListingAmenitiesDto dto);
    @Mapping(source = "listing.id", target = "listingId")
    ListingAmenitiesDto toDto(ListingAmenities entity);
}
