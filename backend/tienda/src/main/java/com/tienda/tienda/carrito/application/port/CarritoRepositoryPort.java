package com.tienda.tienda.carrito.application.port;

import com.tienda.tienda.carrito.domain.Carrito;
import reactor.core.publisher.Mono;

public interface CarritoRepositoryPort {
    Mono<Carrito> findById(int id);
    Mono<Carrito> save(Carrito carrito);
}
