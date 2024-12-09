package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.entity.Listing;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PhotoMapper.class)
public interface ListingMapper {

    @Mapping(target = "photos", ignore = true)
    ListingDto toDto(Listing listing);

    @Mapping(target = "photos", ignore = true)
    Listing toEntity(ListingDto listingDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ListingDto listingDto, @MappingTarget Listing listing);
}
