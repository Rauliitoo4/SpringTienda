package com.tienda.promotionservice.application.port.output;

import com.tienda.promotionservice.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface CreatePromotionOutputPort {
    Mono<Promotion> save(Promotion promotion);
}

