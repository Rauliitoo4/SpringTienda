package com.tienda.tienda.carrito.application.port.input;

import reactor.core.publisher.Mono;

public interface DeleteLineaCarritoInputPort {
    Mono<Boolean> execute(int id);
}