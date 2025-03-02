package com.rentspace.bookingservice.mapper;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking toEntity(BookingDto dto);
    BookingDto toDto(Booking entity);
}
