package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository;

import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.entity.PromotionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PromotionR2dbcRepository extends ReactiveCrudRepository<PromotionEntity, Integer> {
}
