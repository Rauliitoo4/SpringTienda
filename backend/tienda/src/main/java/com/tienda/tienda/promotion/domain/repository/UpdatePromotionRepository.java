package com.tienda.tienda.promotion.domain.repository;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface UpdatePromotionRepository {
    Mono<Promotion> save(Promotion promotion);
}
