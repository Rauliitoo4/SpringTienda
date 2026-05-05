package com.tienda.tienda.product.application.port.output;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Mono;

public interface CreateProductOutputPort {
    Mono<Product> save(Product product);
}
