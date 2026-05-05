package com.tienda.carritoservice.application.port.output;

import com.tienda.carritoservice.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface UpdateCarritoOutputPort {
    Mono<Carrito> save(Carrito carrito);
}