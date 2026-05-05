package com.tienda.tienda.product.application.service;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.application.port.output.ProductPromotionOutputPort;
import com.tienda.tienda.product.domain.model.Size;
import com.tienda.tienda.promotion.application.port.output.GetPromotionOutputPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PromotionLoader {

    private final ProductPromotionOutputPort productPromotionOutputPort;
    private final GetPromotionOutputPort getPromotionOutputPort;

    public PromotionLoader(ProductPromotionOutputPort productPromotionOutputPort, GetPromotionOutputPort getPromotionOutputPort) {
        this.productPromotionOutputPort = productPromotionOutputPort;
        this.getPromotionOutputPort = getPromotionOutputPort;
    }

    public Mono<Product> loadPromotions(Product product) {
        return productPromotionOutputPort
                .findPromotionIdsByProductId(product.getId())
                .flatMap(getPromotionOutputPort::findById)
                .collectList()
                .doOnNext(product::setPromotions)
                .thenReturn(product);
    }
}