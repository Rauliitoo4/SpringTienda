package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.GetPromotionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetPromotionUseCase {

    private final GetPromotionRepository getPromotionRepository;

    public GetPromotionUseCase (GetPromotionRepository getPromotionRepository) {
        this.getPromotionRepository = getPromotionRepository;
    }

    public Mono<Promotion> execute(int id) {
        return getPromotionRepository.findById(id);
    }

    public Flux<Promotion> executeAll() {
        return getPromotionRepository.findAll();
    }
}
