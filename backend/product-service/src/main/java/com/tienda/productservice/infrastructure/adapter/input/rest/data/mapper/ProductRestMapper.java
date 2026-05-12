package com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.productservice.domain.model.Product;
import com.tienda.product.model.ProductRequest;
import com.tienda.product.model.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductRestMapper {
    Product toDomain(ProductRequest request);

    @Mapping(target = "createdAt", expression = "java(product.getCreatedAt() != null ? product.getCreatedAt().atOffset(java.time.ZoneOffset.UTC) : null)")
    ProductResponse toResponse(Product product);
}