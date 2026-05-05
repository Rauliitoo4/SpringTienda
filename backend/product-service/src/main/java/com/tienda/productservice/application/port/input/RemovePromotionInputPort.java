package com.tienda.productservice.application.port.input;

import com.tienda.productservice.domain.model.Product;
import reactor.core.publisher.Mono;

public interface RemovePromotionInputPort {
    Mono<Product> execute(int productId, int promotionId);
}