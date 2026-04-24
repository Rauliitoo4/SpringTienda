package com.tienda.tienda.promotion.application.dto.mapper;

import com.tienda.tienda.promotion.domain.Promotion;
import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionResponse toDTO(Promotion promotion){
        PromotionResponse dto = new PromotionResponse();
        dto.setId(promotion.getId());
        dto.setDiscount(promotion.getDiscount());
        dto.setDescription(promotion.getDescription());
        return dto;
    }

    public Promotion toEntity(PromotionResponse dto) {
        Promotion promo = new Promotion();
        promo.setDescription(dto.getDescription());
        promo.setDiscount(dto.getDiscount());
        return promo;
    }
}
