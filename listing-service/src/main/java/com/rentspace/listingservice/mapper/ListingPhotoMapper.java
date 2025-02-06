package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingPhotoDto;
import com.rentspace.listingservice.entity.ListingPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ListingPhotoMapper {

    @Value("${minio.url}")
    private String baseUrl;

    public abstract ListingPhotoDto toDto(ListingPhoto listingPhoto);
    public abstract ListingPhoto toEntity(ListingPhoto listingPhoto);
    public abstract List<ListingPhotoDto> toDtoList(List<ListingPhoto> listingPhotos);
    public abstract List<ListingPhoto> toEntityList(List<ListingPhotoDto> listingPhotoDtos);

    @Named("buildFullUrl")
    public String buildFullUrl(String filePath) {
        return String.format("%s/%s", baseUrl, filePath);
    }
}
