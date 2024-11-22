package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.PhotosDto;
import com.rentspace.listingservice.entity.Photos;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PhotosMapper implements BaseMapper<Photos, PhotosDto> {

    @Override
    public PhotosDto toDto(Photos entity) {
        return Optional.ofNullable(entity)
                .map(e -> {
                    PhotosDto dto = new PhotosDto();
                    dto.setId(e.getId());
                    dto.setUrl(e.getUrl());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public Photos toEntity(PhotosDto dto) {
        return Optional.ofNullable(dto)
                .map(d -> {
                    Photos entity = new Photos();
                    entity.setId(d.getId());
                    entity.setUrl(d.getUrl());
                    return entity;
                })
                .orElse(null);
    }

    public List<PhotosDto> toListDto(List<Photos> photos) {
        return Optional.ofNullable(photos)
                .map(list -> list.stream().map(this::toDto).toList())
                .orElse(List.of());
    }

    public List<Photos> toListEntity(List<PhotosDto> photosDto) {
        return Optional.ofNullable(photosDto)
                .map(list -> list.stream().map(this::toEntity).toList())
                .orElse(List.of());
    }
}
