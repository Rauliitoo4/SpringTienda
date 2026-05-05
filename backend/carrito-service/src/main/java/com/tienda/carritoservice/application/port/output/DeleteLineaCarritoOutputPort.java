package com.tienda.tienda.carrito.application.port.output;

import reactor.core.publisher.Mono;

public interface DeleteLineaCarritoOutputPort {
    Mono<Void> deleteById(int id);
}