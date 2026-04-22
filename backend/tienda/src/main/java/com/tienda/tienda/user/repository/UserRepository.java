package com.tienda.tienda.user.repository;

import com.tienda.tienda.user.model.User;
import com.tienda.tienda.user.repository.port.UserRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> , UserRepositoryPort {
    Mono<User> save(User user);
}
