package com.tienda.userservice.application.port.output;

import com.tienda.userservice.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetUserOutputPort {
    Mono<User> findById(int id);
    Flux<User> findAll();
    Mono<User> findByEmail(String email);
}
