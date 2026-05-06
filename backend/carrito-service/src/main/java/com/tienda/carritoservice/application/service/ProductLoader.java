package com.tienda.carritoservice.application.service;

import com.tienda.carritoservice.application.model.ProductModel;
import com.tienda.carritoservice.application.port.output.GetProductOutputPort;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLoader {

    private final GetProductOutputPort getProductOutputPort;

    public ProductLoader(GetProductOutputPort getProductOutputPort) {
        this.getProductOutputPort = getProductOutputPort;
    }

    public Mono<ProductModel> loadProduct(int productId) {
        return getProductOutputPort.findById(productId);
    }
}