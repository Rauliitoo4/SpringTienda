package com.tienda.promotionservice.application.usecase;

import com.tienda.promotionservice.application.port.input.UpdatePromotionInputPort;
import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.application.port.output.GetPromotionOutputPort;
import com.tienda.promotionservice.application.port.output.UpdatePromotionOutputPort;
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
