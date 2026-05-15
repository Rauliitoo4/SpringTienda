package com.tienda.userservice.application.port.input;

import com.tienda.userservice.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetUserInputPort {
    Mono<User> execute(int id);
    Flux<User> executeAll();
    Mono<User> executeByEmail(String email);
}
