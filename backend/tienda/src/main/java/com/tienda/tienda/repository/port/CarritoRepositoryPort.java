package com.tienda.tienda.repository.port;

import com.tienda.tienda.model.Carrito;
import reactor.core.publisher.Mono;

public interface CarritoRepositoryPort {
    Mono<Carrito> findById(int id);
    Mono<Carrito> save(Carrito carrito);
}
