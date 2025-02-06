package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.entity.Listing;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ListingPhotoMapper.class)
public interface ListingMapper {

//    @Mapping(target = "listingPhotos", ignore = true)
    ListingDto toDto(Listing listing);

    @Mapping(target = "listingPhotos", ignore = true)
    Listing toEntity(ListingDto listingDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ListingDto listingDto, @MappingTarget Listing listing);
}
