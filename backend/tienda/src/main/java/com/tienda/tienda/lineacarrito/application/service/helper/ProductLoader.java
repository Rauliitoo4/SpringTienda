package com.tienda.tienda.lineacarrito.application.service.helper;

import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.product.application.port.ProductRepositoryPort;
import com.tienda.tienda.product.application.service.helper.PromotionLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLoader {

    private final ProductRepositoryPort productRepo;
    private final PromotionLoader promotionLoader;

    public ProductLoader (ProductRepositoryPort productRepo, PromotionLoader promotionLoader) {
        this.productRepo = productRepo;
        this.promotionLoader = promotionLoader;
    }

    public Mono<LineaCarrito> loadProduct(LineaCarrito linea) {
        return productRepo.findById(linea.getProductId())
                .flatMap(promotionLoader::loadPromotions)
                .doOnNext(linea::setProduct)
                .thenReturn(linea);
    }
}
