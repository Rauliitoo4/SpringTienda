package com.tienda.tienda.carrito.application.port.input;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetLineaCarritoInputPort {
    Mono<LineaCarrito> execute(int id);
    Flux<LineaCarrito> executeAll();
}