package com.tienda.tienda.service;

import com.tienda.tienda.dto.PromotionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PromotionService {
    Mono<PromotionDTO> createPromotion(PromotionDTO dto);
    Mono<PromotionDTO> updatePromotion(int id, PromotionDTO dto);
    Mono<Boolean> deletePromotion(int id);
    Mono<PromotionDTO> getPromotionById(int id);
    Flux<PromotionDTO> getAllPromotions();
}
