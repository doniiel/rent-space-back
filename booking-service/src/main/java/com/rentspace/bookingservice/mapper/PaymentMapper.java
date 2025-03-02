package com.rentspace.bookingservice.mapper;

import com.rentspace.bookingservice.dto.PaymentTransactionDto;
import com.rentspace.bookingservice.entity.PaymentTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentTransaction toEntity(PaymentTransactionDto dto);
    PaymentTransactionDto toDto(PaymentTransaction entity);
}
