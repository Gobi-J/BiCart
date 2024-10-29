package com.bicart.mapper;

import com.bicart.dto.CategoryDto;
import com.bicart.model.Category;

public class CategoryMapper {
    public static Category dtoToModel(CategoryDto category) {
        return Category.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static CategoryDto modelToDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }
}
