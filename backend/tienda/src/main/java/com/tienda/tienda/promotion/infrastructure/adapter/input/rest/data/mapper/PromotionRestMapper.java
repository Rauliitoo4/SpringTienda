package com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request.PromotionRequest;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.springframework.stereotype.Component;

@Component
public class PromotionRestMapper {

    public Promotion toDomain(PromotionRequest request) {
        Promotion promotion = new Promotion();
        promotion.setDiscount(request.getDiscount());
        promotion.setDescription(request.getDescription());
        return promotion;
    }

    public PromotionResponse toResponse(Promotion promotion) {
        PromotionResponse response = new PromotionResponse();
        response.setId(promotion.getId());
        response.setDiscount(promotion.getDiscount());
        response.setDescription(promotion.getDescription());
        return response;
    }
}
