package com.tienda.promotionservice.infrastructure.adapter.output.persistence.mapper;

import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.infrastructure.adapter.output.persistence.entity.PromotionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionPersistenceMapper {
    PromotionEntity toEntity(Promotion promotion);
    Promotion toDomain(PromotionEntity entity);
}
