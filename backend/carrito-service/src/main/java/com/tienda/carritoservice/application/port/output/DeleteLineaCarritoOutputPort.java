package com.tienda.carritoservice.application.port.output;

import reactor.core.publisher.Mono;

public interface DeleteLineaCarritoOutputPort {
    Mono<Void> deleteById(int id);
}