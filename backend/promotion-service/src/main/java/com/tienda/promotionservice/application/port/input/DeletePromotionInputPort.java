package com.tienda.promotionservice.application.port.input;

import reactor.core.publisher.Mono;

public interface DeletePromotionInputPort {
    Mono<Boolean> execute(int id);
}