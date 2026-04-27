package com.tienda.tienda.carrito.domain.repository;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface GetCarritoRepository {
    Mono<Carrito> findById(int id);
}