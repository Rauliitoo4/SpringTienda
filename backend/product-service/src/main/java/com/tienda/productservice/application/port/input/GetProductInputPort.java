package com.tienda.productservice.application.port.input;

import com.tienda.productservice.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetProductInputPort {
    Mono<Product> execute(int id);
    Flux<Product> executeAll();
}