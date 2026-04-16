package com.tienda.tienda.repository.port;

import com.tienda.tienda.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {
    Mono<User> findById(int id);
    Mono<User> save(User user);
    Flux<User> findAll();
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
