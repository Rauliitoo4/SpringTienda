package com.tienda.carritoservice.application.port.input;

import com.tienda.carritoservice.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface GetCarritoInputPort {
    Mono<Carrito> execute(int id);
}