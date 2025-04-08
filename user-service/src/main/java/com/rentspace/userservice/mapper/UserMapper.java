package com.rentspace.userservice.mapper;

import com.rentspace.core.dto.UserDto;
import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = CurrencyMapper.class)
public interface UserMapper {

    User toEntity(UserDto userDto);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "bio", source = "profile.bio")
    @Mapping(target = "avatarUrl", source = "profile.avatarUrl")
    @Mapping(target = "language", source = "profile.language")
    @Mapping(target = "currency", source = "profile.currency", qualifiedByName = "mapCurrency")
    UserDto toResponseDto(User user);

    // Остальные методы
}

@Named("CurrencyMapper")
class CurrencyMapper {
    @Named("mapCurrency")
    public static String mapCurrency(com.rentspace.core.enums.Currency currency) {
        return currency.name();
    }
}