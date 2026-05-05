package com.tienda.promotionservice.application.port.output;

import com.tienda.promotionservice.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface UpdatePromotionOutputPort {
    Mono<Promotion> save(Promotion promotion);
}
