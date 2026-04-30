package com.tienda.tienda.user.application.port.output;

import reactor.core.publisher.Mono;

public interface DeleteUserOutputPort {
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
