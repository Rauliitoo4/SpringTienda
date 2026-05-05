package com.tienda.tienda.product.application.port.input;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductInputPort {
    Mono<Product> execute(int id, Product product);
}