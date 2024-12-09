package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.PhotoDto;
import com.rentspace.listingservice.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PhotoMapper {

    @Value("${minio.url}")
    private String baseUrl;

    @Mapping(target = "url", source = "url", expression = "buildFullUrl")
    public abstract PhotoDto toDto(Photo photo);
    public abstract Photo toEntity(Photo photo);
    public abstract List<PhotoDto> toDtoList(List<Photo> photos);
    public abstract List<Photo> toEntityList(List<PhotoDto> photoDtos);

    @Named("buildFullUrl")
    public String buildFullUrl(String filePath) {
        return String.format("%s/%s", baseUrl, filePath);
    }
}
