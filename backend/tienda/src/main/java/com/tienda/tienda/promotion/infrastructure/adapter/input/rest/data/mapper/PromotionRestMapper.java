package com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request.PromotionRequest;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionRestMapper {
    Promotion toDomain(PromotionRequest request);
    PromotionResponse toResponse(Promotion promotion);
}
