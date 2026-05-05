package com.tienda.productservice.application.service;

import com.tienda.productservice.application.port.output.ProductPromotionOutputPort;
import com.tienda.productservice.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductPromotionOutputPort productPromotionOutputPort;

    public PromotionLoader(ProductPromotionOutputPort productPromotionOutputPort) {
        this.productPromotionOutputPort = productPromotionOutputPort;
    }

    public Mono<Product> loadPromotions(Product product) {
        return productPromotionOutputPort
                .findPromotionIdsByProductId(product.getId())
                .collectList()
                .doOnNext(product::setPromotionIds)
                .thenReturn(product);
    }
}