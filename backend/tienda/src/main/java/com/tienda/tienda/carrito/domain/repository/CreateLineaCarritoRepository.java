package com.tienda.tienda.carrito.domain.repository;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import reactor.core.publisher.Mono;

public interface CreateLineaCarritoRepository {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}