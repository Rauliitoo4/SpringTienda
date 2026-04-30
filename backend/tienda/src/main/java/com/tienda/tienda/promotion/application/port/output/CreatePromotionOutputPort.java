package com.tienda.tienda.promotion.application.port.output;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface CreatePromotionOutputPort {
    Mono<Promotion> save(Promotion promotion);
}

