package com.tienda.promotionservice.application.port.output;

import com.tienda.promotionservice.domain.model.Promotion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPromotionOutputPort {
    Mono<Promotion> findById(int id);
    Flux<Promotion> findAll();
}
