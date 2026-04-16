package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.User;
import com.tienda.tienda.repository.port.UserRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> , UserRepositoryPort {
    Mono<User> save(User user);
}
