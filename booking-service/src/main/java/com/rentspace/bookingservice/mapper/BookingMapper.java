package com.rentspace.bookingservice.mapper;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "status", source = "status")
    Booking toEntity(BookingDto dto);

    @Mapping(target = "status", source = "status")
    BookingDto toDto(Booking entity);
}
