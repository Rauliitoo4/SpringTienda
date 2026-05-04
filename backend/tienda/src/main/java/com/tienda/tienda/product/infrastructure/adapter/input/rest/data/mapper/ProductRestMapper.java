package com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PromotionRestMapper.class})
public interface ProductRestMapper {
    Product toDomain(ProductRequest request);
    @Mapping(target = "createdAt", expression = "java(product.getCreatedAt() != null ? product.getCreatedAt().toString() : null)")
    ProductResponse toResponse(Product product);
}
