package com.tienda.tienda.service.helper;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductPromotionRepository;
import com.tienda.tienda.repository.PromotionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductPromotionRepository productPromotionRepo;
    private final PromotionRepository promotionRepo;

    public PromotionLoader (ProductPromotionRepository productPromotionRepo, PromotionRepository promotionRepo) {
        this.productPromotionRepo = productPromotionRepo;
        this.promotionRepo = promotionRepo;
    }

    public Mono<Product> loadPromotions(Product product) {
        return productPromotionRepo
                .findPromotionIdsByProductId(product.getId())
                .flatMap(promotionRepo::findById)
                .collectList()
                .doOnNext(product::setPromotions)
                .thenReturn(product);
    }
}
