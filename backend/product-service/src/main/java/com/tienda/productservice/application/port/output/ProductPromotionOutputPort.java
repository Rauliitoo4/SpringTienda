package com.tienda.productservice.application.port.output;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPromotionOutputPort {
    Flux<Integer> findPromotionIdsByProductId(int productId);
    Mono<Void> deleteByProductIdAndPromotionId(int productId, int promotionId);
    Mono<Integer> existsRelation(int productId, int promotionId);
    Mono<Void> deleteByPromotionId(int promotionId);
    Mono<Void> insertRelation(int productId, int promotionId);
}