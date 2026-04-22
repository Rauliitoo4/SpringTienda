package com.tienda.tienda.carrito.repository.port;

import com.tienda.tienda.carrito.model.Carrito;
import reactor.core.publisher.Mono;

public interface CarritoRepositoryPort {
    Mono<Carrito> findById(int id);
    Mono<Carrito> save(Carrito carrito);
}
