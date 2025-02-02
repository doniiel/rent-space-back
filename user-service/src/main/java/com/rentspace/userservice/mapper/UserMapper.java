package com.rentspace.userservice.mapper;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserResponseDto;
import com.rentspace.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateDto userCreateDto);
    UserResponseDto toResponseDto(User user);
}
