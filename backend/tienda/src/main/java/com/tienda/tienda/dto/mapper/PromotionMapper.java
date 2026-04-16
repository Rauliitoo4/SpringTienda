package com.tienda.tienda.dto.mapper;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionDTO toDTO(Promotion promotion){
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setDiscount(promotion.getDiscount());
        dto.setDescription(promotion.getDescription());
        return dto;
    }

    public Promotion toEntity(PromotionDTO dto) {
        Promotion promo = new Promotion();
        promo.setDescription(dto.getDescription());
        promo.setDiscount(dto.getDiscount());
        return promo;
    }
}
