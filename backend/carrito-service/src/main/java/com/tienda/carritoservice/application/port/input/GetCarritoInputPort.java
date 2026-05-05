package com.tienda.tienda.carrito.application.port.input;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface GetCarritoInputPort {
    Mono<Carrito> execute(int id);
}