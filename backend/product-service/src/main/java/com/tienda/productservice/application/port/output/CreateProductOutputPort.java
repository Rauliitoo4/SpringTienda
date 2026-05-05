package com.tienda.productservice.application.port.output;

import com.tienda.productservice.domain.model.Product;
import reactor.core.publisher.Mono;

public interface CreateProductOutputPort {
    Mono<Product> save(Product product);
}
