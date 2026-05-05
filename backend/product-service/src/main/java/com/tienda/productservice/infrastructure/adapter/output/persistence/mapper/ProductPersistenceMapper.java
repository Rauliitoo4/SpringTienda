package com.tienda.productservice.infrastructure.adapter.output.persistence.mapper;

import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    @Mapping(target = "category", expression = "java(product.getCategory() != null ? product.getCategory().name() : null)")
    ProductEntity toEntity(Product product);

    @Mapping(target = "promotionIds", ignore = true)
    @Mapping(target = "category", expression = "java(entity.getCategory() != null ? com.tienda.productservice.domain.model.Category.valueOf(entity.getCategory()) : null)")
    Product toDomain(ProductEntity entity);
}