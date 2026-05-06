package com.tienda.carritoservice.application.port.output;

import com.tienda.carritoservice.application.model.ProductModel;
import reactor.core.publisher.Mono;

public interface GetProductOutputPort {
    Mono<ProductModel> findById(int id);
}