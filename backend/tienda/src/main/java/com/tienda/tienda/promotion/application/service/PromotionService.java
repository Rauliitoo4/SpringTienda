package com.tienda.tienda.promotion.application.service;

import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PromotionService {
    Mono<PromotionResponse> createPromotion(PromotionResponse dto);
    Mono<PromotionResponse> updatePromotion(int id, PromotionResponse dto);
    Mono<Boolean> deletePromotion(int id);
    Mono<PromotionResponse> getPromotionById(int id);
    Flux<PromotionResponse> getAllPromotions();
}
