package com.tienda.tienda.user.application.port.input;

import reactor.core.publisher.Mono;

public interface DeleteUserInputPort {
    Mono<Boolean> execute(int id);
}
