package com.tienda.carritoservice.application.port.input;

import reactor.core.publisher.Mono;

public interface DeleteLineaCarritoInputPort {
    Mono<Boolean> execute(int id);
}