package com.bicart.mapper;

import com.bicart.dto.SubCategoryDto;
import com.bicart.model.SubCategory;

public class SubCategoryMapper {
    public static SubCategory dtoToModel(SubCategoryDto subCategory) {
        return SubCategory.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .description(subCategory.getDescription())
                .category(CategoryMapper.dtoToModel(subCategory.getCategory()))
                .build();
    }

    public static SubCategoryDto modelToDto(SubCategory subCategory) {
        return SubCategoryDto.builder()
                .name(subCategory.getName())
                .build();
    }
}
