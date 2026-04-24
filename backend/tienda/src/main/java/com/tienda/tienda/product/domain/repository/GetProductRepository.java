package com.tienda.tienda.product.domain.repository;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetProductRepository {
    Mono<Product> findById(int id);
    Flux<Product> findAll();
}
