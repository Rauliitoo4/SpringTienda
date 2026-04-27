package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.CreatePromotionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreatePromotionUseCase {

    private final CreatePromotionRepository createPromotionRepository;

    public CreatePromotionUseCase(CreatePromotionRepository createPromotionRepository) {
        this.createPromotionRepository = createPromotionRepository;
    }

    public Mono<Promotion> execute(Promotion promotion) {
        return createPromotionRepository.save(promotion);
    }
}
