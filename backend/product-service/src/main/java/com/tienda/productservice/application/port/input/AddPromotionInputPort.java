package com.tienda.tienda.product.application.port.input;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Mono;

public interface AddPromotionInputPort {
    Mono<Product> execute(int productId, int promotionId);
}