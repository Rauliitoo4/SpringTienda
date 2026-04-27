package com.tienda.tienda.product.application.helper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.promotion.domain.repository.GetPromotionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductPromotionRepository productPromotionRepo;
    private final GetPromotionRepository getPromotionRepository;

    public PromotionLoader (ProductPromotionRepository productPromotionRepo, GetPromotionRepository getPromotionRepository) {
        this.productPromotionRepo = productPromotionRepo;
        this.getPromotionRepository = getPromotionRepository;
    }

    public Mono<Product> loadPromotions(Product product) {
        return productPromotionRepo
                .findPromotionIdsByProductId(product.getId())
                .flatMap(getPromotionRepository::findById)
                .collectList()
                .doOnNext(product::setPromotions)
                .thenReturn(product);
    }
}
