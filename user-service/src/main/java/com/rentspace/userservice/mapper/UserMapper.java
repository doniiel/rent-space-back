package com.rentspace.userservice.mapper;

import com.rentspace.core.dto.UserDto;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateRequest request);
    UserDto toResponseDto(User user);
}
