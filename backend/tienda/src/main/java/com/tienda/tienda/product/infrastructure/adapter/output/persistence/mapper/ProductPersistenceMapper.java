package com.tienda.tienda.product.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    ProductEntity toEntity(Product product);
    @Mapping(target = "promotions", ignore = true)
    Product toDomain(ProductEntity entity);
}