package com.tienda.tienda.product.application.helper;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductPromotionRepository productPromotionRepo;
    private final PromotionRepositoryPort promotionRepo;

    public PromotionLoader (ProductPromotionRepository productPromotionRepo, PromotionRepositoryPort promotionRepo) {
        this.productPromotionRepo = productPromotionRepo;
        this.promotionRepo = promotionRepo;
    }

    public Mono<Product> loadPromotions(Product product) {
        return productPromotionRepo
                .findPromotionIdsByProductId(product.getId())
                .flatMap(promotionRepo::findById)
                .collectList()
                .doOnNext(product::setPromotionEntities)
                .thenReturn(product);
    }
}
