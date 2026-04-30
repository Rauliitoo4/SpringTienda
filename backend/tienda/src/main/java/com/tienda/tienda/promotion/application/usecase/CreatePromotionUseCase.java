package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.application.port.input.CreatePromotionInputPort;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.application.port.output.CreatePromotionOutputPort;
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
