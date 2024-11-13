package com.bicart.mapper;

import com.bicart.dto.OrderProductDto;
import com.bicart.dto.ProductDto;
import com.bicart.model.Product;

public class ProductMapper {
    public static Product dtoToModel(ProductDto product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public static ProductDto modelToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .subCategory(SubCategoryMapper.modelToDto(product.getSubCategory()))
                .build();
    }

    public static OrderProductDto modelToOrderDto(Product product) {
        return OrderProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public static Product orderDtoToModel(OrderProductDto product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
