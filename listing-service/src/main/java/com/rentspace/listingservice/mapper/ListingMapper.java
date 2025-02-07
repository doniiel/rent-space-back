package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = ListingPhotoMapper.class)
public interface ListingMapper {

    @Mapping(target = "photoUrls", source = "photos", qualifiedByName = "mapPhotos")
    ListingDto toDto(Listing listing);
    Listing toEntity(ListingCreateRequest request);
    Listing toEntity(ListingUpdateRequest request);
    void updateListingFromRequest(ListingUpdateRequest request, @MappingTarget Listing listing);
    @Named("mapPhotos")
    default List<String> mapPhotos(List<ListingPhoto> photos) {
        return photos != null ? photos.stream()
                .map(ListingPhoto::getPhotoUrl)
                .toList() : null;
    }
}
