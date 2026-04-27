package com.tienda.tienda.promotion.domain.repository;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPromotionRepository {
    Mono<Promotion> findById(int id);
    Flux<Promotion> findAll();
}
