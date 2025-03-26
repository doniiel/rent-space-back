package com.renstapce.review.mapper;

import com.renstapce.review.dto.ReviewCreateRequest;
import com.renstapce.review.dto.ReviewDto;
import com.renstapce.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toEntity(ReviewCreateRequest request);

    ReviewDto toDto(Review review);
}
