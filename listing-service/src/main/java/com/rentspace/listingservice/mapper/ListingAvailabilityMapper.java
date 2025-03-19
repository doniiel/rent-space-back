package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.entity.ListingAvailability;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListingAvailabilityMapper {

    @Mapping(source = "listingId", target = "listing.id")
    ListingAvailability toEntity(ListingAvailabilityDto dto);

    @Mapping(source = "listing.id", target = "listingId")
    ListingAvailabilityDto toDto(ListingAvailability entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ListingAvailabilityDto dto, @MappingTarget ListingAvailability entity);
}