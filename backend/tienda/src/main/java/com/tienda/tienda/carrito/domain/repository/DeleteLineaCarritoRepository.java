package com.tienda.tienda.carrito.domain.repository;

import reactor.core.publisher.Mono;

public interface DeleteLineaCarritoRepository {
    Mono<Void> deleteById(int id);
}