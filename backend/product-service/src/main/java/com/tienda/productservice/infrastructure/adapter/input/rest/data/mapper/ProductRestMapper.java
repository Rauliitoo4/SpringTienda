package com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductRestMapper {
    Product toDomain(ProductRequest request);

    @Mapping(target = "promotions", ignore = true)
    @Mapping(target = "createdAt", expression = "java(product.getCreatedAt() != null ? product.getCreatedAt().toString() : null)")
    ProductResponse toResponse(Product product);
}