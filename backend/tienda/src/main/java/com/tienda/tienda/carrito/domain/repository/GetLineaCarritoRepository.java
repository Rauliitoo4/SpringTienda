package com.tienda.tienda.carrito.domain.repository;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetLineaCarritoRepository {
    Mono<LineaCarrito> findById(int id);
    Flux<LineaCarrito> findAll();
    Flux<LineaCarrito> findByCarritoId(int carritoId);
}