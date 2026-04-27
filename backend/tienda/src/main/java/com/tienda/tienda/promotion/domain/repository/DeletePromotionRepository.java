package com.tienda.tienda.promotion.domain.repository;

import reactor.core.publisher.Mono;

public interface DeletePromotionRepository {
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
