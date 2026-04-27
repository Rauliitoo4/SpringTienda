package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.GetPromotionRepository;
import com.tienda.tienda.promotion.domain.repository.UpdatePromotionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdatePromotionUseCase {

    private final UpdatePromotionRepository updatePromotionRepository;
    private final GetPromotionRepository getPromotionRepository;

    public UpdatePromotionUseCase(UpdatePromotionRepository updatePromotionRepository, GetPromotionRepository getPromotionRepository) {
        this.updatePromotionRepository = updatePromotionRepository;
        this.getPromotionRepository = getPromotionRepository;
    }

    public Mono<Promotion> execute (int id, Promotion updatedPromotion) {
        return getPromotionRepository.findById(id)
                .flatMap(promo -> {
                    if (updatedPromotion.getDescription() != null) promo.setDescription(updatedPromotion.getDescription());
                    if (updatedPromotion.getDiscount() > 0) promo.setDiscount(updatedPromotion.getDiscount());
                    return updatePromotionRepository.save(promo);
                });
    }
}
