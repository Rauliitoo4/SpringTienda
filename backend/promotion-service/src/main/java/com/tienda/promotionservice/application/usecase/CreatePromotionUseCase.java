package com.tienda.promotionservice.application.usecase;

import com.tienda.promotionservice.application.port.input.CreatePromotionInputPort;
import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.application.port.output.CreatePromotionOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreatePromotionUseCase implements CreatePromotionInputPort {

    private final CreatePromotionOutputPort createPromotionOutputPort;

    public CreatePromotionUseCase(CreatePromotionOutputPort createPromotionOutputPort) {
        this.createPromotionOutputPort = createPromotionOutputPort;
    }

    public Mono<Promotion> execute(Promotion promotion) {
        return createPromotionOutputPort.save(promotion);
    }
}
