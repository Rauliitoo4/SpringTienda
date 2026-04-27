package com.tienda.tienda.carrito.application.helper;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLoader {

    private final GetProductRepository getProductRepository;
    private final PromotionLoader promotionLoader;

    public ProductLoader (GetProductRepository getProductRepository, PromotionLoader promotionLoader) {
        this.getProductRepository = getProductRepository;
        this.promotionLoader = promotionLoader;
    }

    public Mono<LineaCarrito> loadProduct(LineaCarrito linea) {
        return getProductRepository.findById(linea.getProductId())
                .flatMap(promotionLoader::loadPromotions)
                .doOnNext(linea::setProduct)
                .thenReturn(linea);
    }
}
