package com.tienda.tienda.promotion.application.port.output;

import reactor.core.publisher.Mono;

public interface DeletePromotionOutputPort {
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
