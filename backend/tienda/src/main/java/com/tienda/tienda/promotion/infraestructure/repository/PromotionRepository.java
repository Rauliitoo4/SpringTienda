package com.tienda.tienda.promotion.infraestructure.repository;

import com.tienda.tienda.promotion.domain.Promotion;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PromotionRepository extends ReactiveCrudRepository<Promotion, Integer> , PromotionRepositoryPort {
    Mono<Promotion> save(Promotion promotion);
}
