package com.tienda.tienda.product.domain.repository;

import reactor.core.publisher.Mono;

public interface DeleteProductRepository {
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
