package com.tienda.productservice.application.port.input;

import com.tienda.productservice.domain.model.Product;
import reactor.core.publisher.Mono;

public interface CreateProductInputPort {
    Mono<Product> execute(Product product);
}