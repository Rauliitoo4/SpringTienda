package com.tienda.tienda.product.application.port.output;

import reactor.core.publisher.Mono;

public interface DeleteProductOutputPort {
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
