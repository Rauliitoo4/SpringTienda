package com.tienda.tienda.product.application.port.input;

import reactor.core.publisher.Mono;

public interface DeleteProductInputPort {
    Mono<Boolean> execute(int id);
}