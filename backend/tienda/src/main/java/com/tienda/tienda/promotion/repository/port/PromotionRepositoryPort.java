package com.tienda.tienda.promotion.repository.port;

import com.tienda.tienda.promotion.model.Promotion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PromotionRepositoryPort {
    Mono<Promotion> findById(int id);
    Mono<Promotion> save(Promotion promotion);
    Flux<Promotion> findAll();
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
