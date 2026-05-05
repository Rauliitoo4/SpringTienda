package com.tienda.productservice.application.port.output;

import com.tienda.productservice.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductOutputPort {
    Mono<Product> save(Product product);
}
