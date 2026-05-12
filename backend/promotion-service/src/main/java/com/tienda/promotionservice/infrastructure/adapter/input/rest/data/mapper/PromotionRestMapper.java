package com.tienda.promotionservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotion.model.PromotionRequest;
import com.tienda.promotion.model.PromotionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionRestMapper {
    Promotion toDomain(PromotionRequest request);
    PromotionResponse toResponse(Promotion promotion);
}
