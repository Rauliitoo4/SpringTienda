package com.tienda.tienda.user.application.port.input;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetUserInputPort {
    Mono<User> execute(int id);
    Flux<User> executeAll();
}
