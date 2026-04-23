package com.tienda.tienda.user.infraestructure.repository;

import com.tienda.tienda.user.domain.User;
import com.tienda.tienda.user.application.port.UserRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> , UserRepositoryPort {
    Mono<User> save(User user);
}
