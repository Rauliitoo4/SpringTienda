package com.tienda.carritoservice.application.port.input;

import com.tienda.carritoservice.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface CreateCarritoInputPort {
    Mono<Carrito> execute();
}