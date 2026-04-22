package com.tienda.tienda.lineacarrito.service.helper;

import com.tienda.tienda.lineacarrito.model.LineaCarrito;
import com.tienda.tienda.product.repository.ProductRepository;
import com.tienda.tienda.product.service.helper.PromotionLoader;
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
                .doOnNext(linea::setProduct)
                .thenReturn(linea);
    }
}
