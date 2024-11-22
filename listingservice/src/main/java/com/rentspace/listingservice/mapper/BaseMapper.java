package com.rentspace.listingservice.mapper;

public interface BaseMapper<E, D> {

    D toDto(E entity);
    E toEntity(D dto);
}
