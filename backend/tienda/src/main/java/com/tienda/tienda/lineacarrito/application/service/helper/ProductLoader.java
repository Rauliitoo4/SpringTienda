package com.tienda.tienda.lineacarrito.application.service.helper;

import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLoader {

    private final ProductRepository productRepo;
    private final PromotionLoader promotionLoader;

    public ProductLoader (ProductRepository productRepo, PromotionLoader promotionLoader) {
        this.productRepo = productRepo;
        this.promotionLoader = promotionLoader;
    }

    public Mono<LineaCarrito> loadProduct(LineaCarrito linea) {
        return productRepo.findById(linea.getProductId())
                .flatMap(promotionLoader::loadPromotions)
                .doOnNext(linea::setProductEntity)
                .thenReturn(linea);
    }
}
