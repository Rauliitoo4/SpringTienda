package com.tienda.tienda.service.helper;

import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.repository.ProductRepository;
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

    public Mono<LineaCarrito> cargarProducto(LineaCarrito linea) {
        return productRepo.findById(linea.getProductoId())
                .flatMap(promotionLoader::cargarPromociones)
                .doOnNext(linea::setProducto)
                .thenReturn(linea);
    }
}
