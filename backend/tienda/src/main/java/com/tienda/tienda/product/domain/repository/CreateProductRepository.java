package com.tienda.tienda.product.domain.repository;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Mono;

public interface CreateProductRepository {
    Mono<Product> save(Product product);
}
