package com.rentspace.userservice.mapper;

import com.rentspace.core.dto.UserDto;
import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "role", defaultValue = "USER")
    User toEntity(UserCreateRequest request);

    @Mapping(target = "role", source = "role.name")
    @Mapping(target = "bio", source = "profile.bio")
    @Mapping(target = "avatarUrl", source = "profile.avatarUrl")
    @Mapping(target = "language", source = "profile.language")
    @Mapping(target = "currency", source = "profile.currency")
    @Mapping(target = "profileCreatedAt", source = "profile.createdAt")
    @Mapping(target = "profileUpdatedAt", source = "profile.updatedAt")
    UserDto toResponseDto(User user);

    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);
}
