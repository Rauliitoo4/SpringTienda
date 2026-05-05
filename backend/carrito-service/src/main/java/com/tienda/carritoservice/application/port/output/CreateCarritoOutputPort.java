package com.tienda.tienda.carrito.application.port.output;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface CreateCarritoOutputPort {
    Mono<Carrito> save(Carrito carrito);
}