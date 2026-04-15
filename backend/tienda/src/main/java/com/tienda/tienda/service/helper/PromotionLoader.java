package com.tienda.tienda.service.helper;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductoPromocionRepository;
import com.tienda.tienda.repository.PromotionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PromotionLoader {

    private final ProductoPromocionRepository productoPromocionRepo;
    private final PromotionRepository promotionRepo;

    public PromotionLoader (ProductoPromocionRepository productoPromocionRepo, PromotionRepository promotionRepo) {
        this.productoPromocionRepo = productoPromocionRepo;
        this.promotionRepo = promotionRepo;
    }

    public Mono<Product> cargarPromociones(Product product) {
        return productoPromocionRepo
                .findPromotionIdsByProductId(product.getId())
                .flatMap(promotionRepo::findById)
                .collectList()
                .doOnNext(product::setPromociones)
                .thenReturn(product);
    }
}
