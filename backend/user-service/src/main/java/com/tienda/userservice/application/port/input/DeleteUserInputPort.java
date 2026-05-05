package com.tienda.userservice.application.port.input;

import reactor.core.publisher.Mono;

public interface DeleteUserInputPort {
    Mono<Boolean> execute(int id);
}
