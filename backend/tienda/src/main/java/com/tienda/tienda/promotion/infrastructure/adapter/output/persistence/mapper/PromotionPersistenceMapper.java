package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.entity.PromotionEntity;
import org.springframework.stereotype.Component;

@Component
public class PromotionPersistenceMapper {

    public PromotionEntity toEntity(Promotion promotion){
        PromotionEntity entity = new PromotionEntity();
        entity.setId(promotion.getId());
        entity.setDescription(promotion.getDescription());
        entity.setDiscount(promotion.getDiscount());
        return entity;
    }

    public Promotion toDomain(PromotionEntity entity){
        Promotion promotion = new Promotion();
        promotion.setId(entity.getId());
        promotion.setDescription(entity.getDescription());
        promotion.setDiscount(entity.getDiscount());
        return promotion;
    }
}
