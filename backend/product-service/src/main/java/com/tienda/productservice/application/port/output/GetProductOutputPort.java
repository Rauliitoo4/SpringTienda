package com.tienda.tienda.product.application.port.output;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetProductOutputPort {
    Mono<Product> findById(int id);
    Flux<Product> findAll();
}
