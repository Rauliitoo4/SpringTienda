package com.tienda.tienda.dto.mapper;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionDTO toDTO(Promotion promotion){
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setDescuento(promotion.getDescuento());
        dto.setDescripcion(promotion.getDescripcion());
        return dto;
    }

    public Promotion toEntity(PromotionDTO dto) {
        Promotion promo = new Promotion();
        promo.setDescripcion(dto.getDescripcion());
        promo.setDescuento(dto.getDescuento());
        return promo;
    }
}
