package com.bicart.mapper;

import com.bicart.dto.ProductDto;
import com.bicart.model.Product;

public class ProductMapper {
    public static Product dtoToModel(ProductDto product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public static ProductDto modelToDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

}
