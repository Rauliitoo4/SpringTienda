package com.tienda.tienda.user.repository.port;

import com.tienda.tienda.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {
    Mono<User> findById(int id);
    Mono<User> save(User user);
    Flux<User> findAll();
    Mono<Boolean> existsById(int id);
    Mono<Void> deleteById(int id);
}
