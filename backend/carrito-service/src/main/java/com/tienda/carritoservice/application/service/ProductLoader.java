package com.tienda.carritoservice.application.service;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.product.application.port.output.GetProductOutputPort;
import com.tienda.tienda.product.application.service.PromotionLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLoader {

    private final GetProductOutputPort getProductOutputPort;
    private final PromotionLoader promotionLoader;

    public ProductLoader (GetProductOutputPort getProductOutputPort, PromotionLoader promotionLoader) {
        this.getProductOutputPort = getProductOutputPort;
        this.promotionLoader = promotionLoader;
    }

    public Mono<LineaCarrito> loadProduct(LineaCarrito linea) {
        return getProductOutputPort.findById(linea.getProductId())
                .flatMap(promotionLoader::loadPromotions)
                .doOnNext(linea::setProduct)
                .thenReturn(linea);
    }
}
