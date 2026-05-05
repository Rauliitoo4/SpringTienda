package com.tienda.carritoservice.application.port.input;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetLineaCarritoInputPort {
    Mono<LineaCarrito> execute(int id);
    Flux<LineaCarrito> executeAll();
}