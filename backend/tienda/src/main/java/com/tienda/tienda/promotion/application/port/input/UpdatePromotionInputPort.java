package com.tienda.tienda.promotion.application.port.input;

import com.tienda.tienda.promotion.domain.model.Promotion;
import reactor.core.publisher.Mono;

public interface UpdatePromotionInputPort {
    Mono<Promotion> execute(int id, Promotion promotion);
}