package com.rentspace.paymentservice.mapper;

import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    PaymentDto toDto(Payment entity);
    Payment toEntity(PaymentDto dto);
}
