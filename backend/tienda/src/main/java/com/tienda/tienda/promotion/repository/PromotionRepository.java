package com.tienda.tienda.promotion.repository;

import com.tienda.tienda.promotion.model.Promotion;
import com.tienda.tienda.promotion.repository.port.PromotionRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PromotionRepository extends ReactiveCrudRepository<Promotion, Integer> , PromotionRepositoryPort {
    Mono<Promotion> save(Promotion promotion);
}
