package com.tienda.tienda.promotion.application.port.output;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPromotionOutputPort {
    Mono<Promotion> findById(int id);
    Flux<Promotion> findAll();
}
