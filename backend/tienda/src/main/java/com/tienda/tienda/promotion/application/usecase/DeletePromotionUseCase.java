package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.application.port.input.DeletePromotionInputPort;
import com.tienda.tienda.promotion.application.port.output.DeletePromotionOutputPort;
import com.tienda.tienda.product.application.port.output.ProductPromotionOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeletePromotionUseCase implements DeletePromotionInputPort {

    private final DeletePromotionOutputPort deletePromotionOutputPort;
    private final ProductPromotionOutputPort productPromotionOutputPort;

    public DeletePromotionUseCase(DeletePromotionOutputPort deletePromotionOutputPort, ProductPromotionOutputPort productPromotionOutputPort) {
        this.deletePromotionOutputPort = deletePromotionOutputPort;
        this.productPromotionOutputPort = productPromotionOutputPort;
    }

    public Mono<Boolean> execute(int id) {
        return deletePromotionOutputPort.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return productPromotionOutputPort.deleteByPromotionId(id)
                            .then(deletePromotionOutputPort.deleteById(id))
                            .thenReturn(true);
                });
    }
}
