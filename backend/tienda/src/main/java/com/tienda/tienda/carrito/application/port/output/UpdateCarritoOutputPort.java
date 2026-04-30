package com.tienda.tienda.carrito.application.port.output;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface UpdateCarritoOutputPort {
    Mono<Carrito> save(Carrito carrito);
}