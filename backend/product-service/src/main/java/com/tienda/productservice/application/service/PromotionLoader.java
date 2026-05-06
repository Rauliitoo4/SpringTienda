package com.tienda.productservice.application.service;

import com.tienda.productservice.application.model.PromotionModel;
import com.tienda.productservice.application.port.output.GetPromotionOutputPort;
import com.tienda.productservice.application.port.output.ProductPromotionOutputPort;
import com.tienda.productservice.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductPromotionOutputPort productPromotionOutputPort;
    private final GetPromotionOutputPort getPromotionOutputPort;
    private final PriceCalculator priceCalculator;

    public PromotionLoader(ProductPromotionOutputPort productPromotionOutputPort,
                           GetPromotionOutputPort getPromotionOutputPort,
                           PriceCalculator priceCalculator) {
        this.productPromotionOutputPort = productPromotionOutputPort;
        this.getPromotionOutputPort = getPromotionOutputPort;
        this.priceCalculator = priceCalculator;
    }

    public Mono<Product> loadPromotions(Product product) {
        return productPromotionOutputPort
                .findPromotionIdsByProductId(product.getId())
                .collectList()
                .flatMap(ids -> getPromotionOutputPort.findAllByIds(ids).collectList())
                .doOnNext(promotions -> {
                    product.setPromotionIds(promotions.stream().map(PromotionModel::getId).toList());
                    priceCalculator.recalculateFinalPrice(product, promotions);
                })
                .thenReturn(product);
    }
}