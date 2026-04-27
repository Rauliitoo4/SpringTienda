package com.tienda.tienda.carrito.domain.repository;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface UpdateCarritoRepository {
    Mono<Carrito> save(Carrito carrito);
}