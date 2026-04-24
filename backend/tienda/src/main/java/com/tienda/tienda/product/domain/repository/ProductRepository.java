package com.tienda.tienda.product.domain.repository;

import com.tienda.tienda.product.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> findById(int id);
    Mono<Product> save(Product product);
    Flux<Product> findAll();
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
