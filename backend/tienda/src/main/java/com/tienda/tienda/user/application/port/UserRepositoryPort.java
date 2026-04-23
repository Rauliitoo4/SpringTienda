package com.tienda.tienda.user.application.port;

import com.tienda.tienda.user.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {
    Mono<User> findById(int id);
    Mono<User> save(User user);
    Flux<User> findAll();
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
