package com.tienda.promotionservice.application.port.input;

import com.tienda.promotionservice.domain.model.Promotion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPromotionInputPort {
    Mono<Promotion> execute(int id);
    Flux<Promotion> executeAll();
}