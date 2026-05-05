package com.tienda.promotionservice.application.port.input;

import com.tienda.promotionservice.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface UpdatePromotionInputPort {
    Mono<Promotion> execute(int id, Promotion promotion);
}