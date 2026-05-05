package com.tienda.carritoservice.application.port.input;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface CreateCarritoInputPort {
    Mono<Carrito> execute();
}