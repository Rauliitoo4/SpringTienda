package com.tienda.tienda.product.application.port.output;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductOutputPort {
    Mono<Product> save(Product product);
}
