package com.tienda.tienda.promotion.application.port.input;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface CreatePromotionInputPort {
    Mono<Promotion> execute(Promotion promotion);
}