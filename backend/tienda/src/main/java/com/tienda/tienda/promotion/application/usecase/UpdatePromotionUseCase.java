package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.application.port.input.UpdatePromotionInputPort;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.application.port.output.GetPromotionOutputPort;
import com.tienda.tienda.promotion.application.port.output.UpdatePromotionOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdatePromotionUseCase implements UpdatePromotionInputPort {

    private final UpdatePromotionOutputPort updatePromotionOutputPort;
    private final GetPromotionOutputPort getPromotionOutputPort;

    public UpdatePromotionUseCase(UpdatePromotionOutputPort updatePromotionOutputPort, GetPromotionOutputPort getPromotionOutputPort) {
        this.updatePromotionOutputPort = updatePromotionOutputPort;
        this.getPromotionOutputPort = getPromotionOutputPort;
    }

    public Mono<Promotion> execute (int id, Promotion updatedPromotion) {
        return getPromotionOutputPort.findById(id)
                .flatMap(promo -> {
                    if (updatedPromotion.getDescription() != null) promo.setDescription(updatedPromotion.getDescription());
                    if (updatedPromotion.getDiscount() > 0) promo.setDiscount(updatedPromotion.getDiscount());
                    return updatePromotionOutputPort.save(promo);
                });
    }
}
