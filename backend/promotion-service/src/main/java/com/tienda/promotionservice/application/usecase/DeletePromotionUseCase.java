package com.tienda.promotionservice.application.usecase;

import com.tienda.promotionservice.application.port.input.DeletePromotionInputPort;
import com.tienda.promotionservice.application.port.output.DeletePromotionOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeletePromotionUseCase implements DeletePromotionInputPort {

    private final DeletePromotionOutputPort deletePromotionOutputPort;

    public DeletePromotionUseCase(DeletePromotionOutputPort deletePromotionOutputPort) {
        this.deletePromotionOutputPort = deletePromotionOutputPort;
    }

    public Mono<Boolean> execute(int id) {
        return deletePromotionOutputPort.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return deletePromotionOutputPort.deleteById(id)
                            .thenReturn(true);
                });
    }
}