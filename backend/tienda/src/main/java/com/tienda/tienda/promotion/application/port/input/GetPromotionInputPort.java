package com.tienda.tienda.promotion.application.port.input;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPromotionInputPort {
    Mono<Promotion> execute(int id);
    Flux<Promotion> executeAll();
}