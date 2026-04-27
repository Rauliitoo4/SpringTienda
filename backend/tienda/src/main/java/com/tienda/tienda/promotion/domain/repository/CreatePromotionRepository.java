package com.tienda.tienda.promotion.domain.repository;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface CreatePromotionRepository {
    Mono<Promotion> save(Promotion promotion);
}

