package com.tienda.tienda.product.application.service.helper;

import com.tienda.tienda.product.domain.Product;
import com.tienda.tienda.product.application.port.ProductPromotionRepositoryPort;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductPromotionRepositoryPort productPromotionRepo;
    private final PromotionRepositoryPort promotionRepo;

    public PromotionLoader (ProductPromotionRepositoryPort productPromotionRepo, PromotionRepositoryPort promotionRepo) {
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
