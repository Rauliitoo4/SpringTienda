package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.application.port.input.GetPromotionInputPort;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.application.port.output.GetPromotionOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetPromotionUseCase implements GetPromotionInputPort {

    private final GetPromotionOutputPort getPromotionOutputPort;

    public GetPromotionUseCase (GetPromotionOutputPort getPromotionOutputPort) {
        this.getPromotionOutputPort = getPromotionOutputPort;
    }

    public Mono<Promotion> execute(int id) {
        return getPromotionOutputPort.findById(id);
    }

    public Flux<Promotion> executeAll() {
        return getPromotionOutputPort.findAll();
    }
}
