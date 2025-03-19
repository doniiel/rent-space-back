package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.entity.ListingAmenities;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListingAmenitiesMapper {

    @Mapping(source = "listingId", target = "listing.id")
    ListingAmenities toEntity(ListingAmenitiesDto dto);

    @Mapping(source = "listing.id", target = "listingId")
    ListingAmenitiesDto toDto(ListingAmenities entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ListingAmenitiesDto dto, @MappingTarget ListingAmenities entity);
}