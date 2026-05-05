package com.tienda.tienda.product.application.port.input;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetProductInputPort {
    Mono<Product> execute(int id);
    Flux<Product> executeAll();
}