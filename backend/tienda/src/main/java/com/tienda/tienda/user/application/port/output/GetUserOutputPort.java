package com.tienda.tienda.user.application.port.output;

import com.tienda.tienda.user.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetUserOutputPort {
    Mono<User> findById(int id);
    Flux<User> findAll();
}
