package com.rentspace.listingservice.mapper;

import com.rentspace.listingservice.dto.ListingsDto;
import com.rentspace.listingservice.entity.Listings;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ListingsMapper implements BaseMapper<Listings, ListingsDto>{

    @Override
    public ListingsDto toDto(Listings entity) {
        return Optional.ofNullable(entity)
                .map(e -> {
                    ListingsDto dto = new ListingsDto();
                    dto.setTitle(e.getTitle());
                    dto.setDescription(e.getDescription());
                    dto.setPricePerNight(e.getPricePerNight());
                    dto.setLocation(e.getLocation());
                    dto.setUserId(e.getUserId());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public Listings toEntity(ListingsDto dto) {
        return Optional.ofNullable(dto)
                .map(d -> {
                    Listings entity = new Listings();
                    entity.setTitle(d.getTitle());
                    entity.setDescription(d.getDescription());
                    entity.setPricePerNight(d.getPricePerNight());
                    entity.setLocation(d.getLocation());
                    entity.setUserId(d.getUserId());
                    return entity;
                        })
                .orElse(null);
    }
}
