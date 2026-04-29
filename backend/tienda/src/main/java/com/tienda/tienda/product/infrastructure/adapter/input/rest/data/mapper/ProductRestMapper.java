package com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PromotionRestMapper.class})
public interface ProductRestMapper {
    Product toDomain(ProductRequest request);
    ProductResponse toResponse(Product product);
}
